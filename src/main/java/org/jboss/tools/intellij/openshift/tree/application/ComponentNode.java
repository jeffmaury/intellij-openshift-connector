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
package org.jboss.tools.intellij.openshift.tree.application;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.jboss.tools.intellij.openshift.tree.KubernetesResourceMutableTreeNode;
import org.jboss.tools.intellij.openshift.utils.odo.Odo;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;

public class ComponentNode extends KubernetesResourceMutableTreeNode {
  public ComponentNode(HasMetadata componentResource) {
    super(componentResource);
  }

  @Override
  public void load() {
    super.load();
    try {
      ApplicationsRootNode clusterNode = (ApplicationsRootNode) getRoot();
      Odo.get().getStorages(clusterNode.getClient(), getParent().getParent().toString(), getParent().toString(), toString()).forEach(pvc -> add(new PersistentVolumeClaimNode(pvc)));
    } catch (KubernetesClientException|IOException e) {
      add(new DefaultMutableTreeNode("Failed to load persistent volume claims"));
    }

  }

  @Override
  public String getIconName() {
    return "/images/component.png";
  }
}
