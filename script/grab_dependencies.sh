#! /bin/bash

# Kurt Harriger's export script
# GitHub: https://github.com/kurtharriger
# Web site: http://blog.kurtharriger.com
# E-mail: kurtharriger@gmail.com

set -e 
trap "exit 1;" INT TERM EXIT

# exec > >(tee $0.log)
# exec 2>&1

[ -z "$CRX_URL" ] && CRX_URL=http://localhost:4502 
[ -z "$CRX_CREDENTIALS" ] && CRX_CREDENTIALS=admin:admin


mkdir -p cq5-libs
pushd cq5-libs > /dev/null

# Get list of jars from crxde classpath in crx
if [ ! -f crxde-classpath ]; then
	curl -s -H x-crxde-version:1.0 -H x-crxde-os:mac -H x-crxde-profile:default -u $CRX_CREDENTIALS $CRX_URL/bin/crxde.classpath.xml \
	| sed -n '/lib/s/.*WebContent\(.*\)\".*/\1/p' \
	> crxde-classpath
fi

# Download each jar file from crx
for file in $(cat crxde-classpath); do
	if [ ! -f $(basename "$file") ]; then
		echo -n "Downloading $file..."
		curl -s -u $CRX_CREDENTIALS $CRX_URL$file -O 
		echo "done."
	else
		echo "Skipped $file.  Already exists."
	fi
done

function extract-pom-files() {
  local file="$1"
  if [ ! -f "$file-pom.properties" ]; then
    echo -n "Extracting pom files from $file..."
    rm -rf "$file.tmp"
    mkdir "$file.tmp"
    pushd "$file.tmp" > /dev/null 
    jar xvf "../$file" META-INF/maven >/dev/null 2>&1 || true
    if find . -name "pom.xml" -exec mv {} "../$file-pom.xml" \; ; then
      find . -name "pom.properties" -exec mv {} "../$file-pom.properties" \; || true
      echo "done."
    else 
      echo "pom.xml file not found.";
    fi
    popd > /dev/null
    rm -rf "$file.tmp"
  fi
  [ -f "$file-pom.properties" ] # return value
}


function read-pom-properties() {
  pom_version=$(grep version "$1" | cut -d= -f2)
  pom_groupId=$(grep groupId "$1" | cut -d= -f2)
  pom_artifactId=$(grep artifactId "$1" | cut -d= -f2)
}

function read-pom-dependencies() {
  pom_dependencies=$(sed -n '/<dependencies>/,/<\/dependencies>/p' "$1")
}

function generate-clean-pom-file() {
    cat <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>$pom_groupId</groupId>
    <artifactId>$pom_artifactId</artifactId>
    <version>$pom_version</version>

    $pom_dependencies

</project>
EOF
}

function mvn-install() {
  local file="$1"
  local pom="$2"
  echo -n "Installing $file in maven repository..."
  if mvn install:install-file "-Dfile=$file" "-DpomFile=$pom"  >mvn.log 2>&1; then
    echo "done."
  else 
    mkdir -p errors
    cp "$file"* errors
      
    echo ""
    echo "Error occured installing $file to maven repository.  See mvn.log for details."
    echo "This may occur when pom.xml file uses variables.  Modify pom.xml and retry."
    echo "mvn install:install-file -Dfile=$file -DpomFile=$file-pom.xml"
    echo "Files copied to errors folder."
  fi
}

for file in *.jar; do
  if extract-pom-files "$file"; then
    read-pom-properties "$file-pom.properties"
    read-pom-dependencies "$file-pom.xml"
    generate-clean-pom-file > "$file-pom.clean.xml"
    mvn-install "$file" "$file-pom.clean.xml"
  else
    echo "Skipped $file.  Does not contain a pom.xml"
    mkdir -p errors
    cp "$file" errors
  fi
done

popd > /dev/null