/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.intellij.openshift.actions.cluster;

import com.intellij.openapi.actionSystem.AnActionEvent;
import io.fabric8.kubernetes.api.model.NamedContext;
import org.jboss.tools.intellij.openshift.actions.TreeAction;
import org.jboss.tools.intellij.openshift.utils.ConfigHelper;
import org.jboss.tools.intellij.openshift.tree.ClustersTreeModel;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import java.io.IOException;

public class DeleteFromKubeConfigAction extends TreeAction {
    public DeleteFromKubeConfigAction() {
        super(NamedContext.class);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent, TreePath path, Object selected) {
        NamedContext context = (NamedContext) selected;
        ClustersTreeModel model = (ClustersTreeModel) getTree(anActionEvent).getModel();
        model.getConfig().getContexts().remove(context);
        try {
            ConfigHelper.saveKubeConfig(model.getConfig());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getLocalizedMessage(), "Delete from Kube config", JOptionPane.ERROR_MESSAGE);
        }
    }
}
