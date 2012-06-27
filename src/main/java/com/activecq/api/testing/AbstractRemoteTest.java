/*
 * Copyright 2012 david.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.activecq.api.testing;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jcr.*;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

/**
 *
 * @author david
 */
public abstract class AbstractRemoteTest {

    private ResourceResolverFactory resourceResolverFactory;
    private ResourceResolver resourceResolver;
    protected RemoteTester remote;

    public void setUp(ResourceResolverFactory resourceResolverFactory) throws Exception {
        this.resourceResolverFactory = resourceResolverFactory;
        getResourceResolver();
    }

    public void tearDown() throws Exception {
        closeResourceResolver();
    }

    protected void setRemoteTester(RemoteTester remote) {
        this.remote = remote;
    }

    /**
     * Private Method *
     */
    protected ResourceResolver getResourceResolver() {
        if (this.resourceResolver == null || !this.resourceResolver.isLive()) {
            try {
                this.resourceResolver = this.resourceResolverFactory.getAdministrativeResourceResolver(null);
            } catch (LoginException ex) {
                Logger.getLogger(AbstractRemoteTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return this.resourceResolver;
    }

    protected Session getSession() {
        return getResourceResolver().adaptTo(Session.class);
    }

    protected void closeResourceResolver() {
        if (resourceResolver != null) {
            resourceResolver.close();
            resourceResolver = null;
        }
    }

    protected void removeNodes(ResourceResolver resourceResolver, String... paths) {
        try {
            Session session = resourceResolver.adaptTo(Session.class);

            if (paths == null || session == null) {
                return;
            }

            for (int i = 0; i < paths.length; i++) {
                final String path = paths[i];
                session.removeItem(path);
            }

            session.save();
        } catch (AccessDeniedException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ItemExistsException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReferentialIntegrityException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConstraintViolationException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidItemStateException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (VersionException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LockException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchNodeTypeException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RepositoryException ex) {
            Logger.getLogger(RemoteTester.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
