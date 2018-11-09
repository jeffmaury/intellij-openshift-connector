package org.jboss.tools.intellij.openshift.tree.application;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.tree.BaseTreeModel;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.openshift.client.DefaultOpenShiftClient;
import io.fabric8.openshift.client.OpenShiftClient;
import org.jboss.tools.intellij.openshift.tree.LazyMutableTreeNode;
import org.jboss.tools.intellij.openshift.tree.RefreshableTreeModel;
import org.jboss.tools.intellij.openshift.utils.ConfigHelper;
import org.jboss.tools.intellij.openshift.utils.ConfigWatcher;

import javax.swing.tree.TreePath;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ApplicationTreeModel extends BaseTreeModel<Object> implements ConfigWatcher.Listener, RefreshableTreeModel {
    private LazyMutableTreeNode ROOT;

    private static final String ERROR = "Please login to cluster first";
    private OpenShiftClient client;

    public ApplicationTreeModel() {
        client = loadClient();
        CompletableFuture.runAsync(new ConfigWatcher(new File(ConfigHelper.getKubeConfigPath()), this));
        ROOT = new ApplicationsRootNode(client);
    }

    protected OpenShiftClient loadClient() {
        return new DefaultOpenShiftClient(new ConfigBuilder().build());
    }

    public OpenShiftClient getClient() {
        return client;
    }

    @Override
    public List<? extends Object> getChildren(Object o) {
        if (o instanceof LazyMutableTreeNode) {
            LazyMutableTreeNode node = (LazyMutableTreeNode) o;
            CompletableFuture.runAsync(() -> {
                if (node.load()) {
                    ApplicationManager.getApplication().invokeLater(() -> this.treeNodesChanged(new TreePath(o), new int[0], new Object[0]));
                }
            });
            return Collections.list(node.children());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Object getRoot() {
        return ROOT;
    }

    @Override
    public void onUpdate(ConfigWatcher source) {
        refresh();
    }

    @Override
    public void refresh() {
        TreePath path = new TreePath(ROOT);
        client = loadClient();
        ROOT.reload();
        this.treeStructureChanged(path, new int[0], new Object[0]);
    }

    @Override
    public void treeStructureChanged(TreePath path, int[] indices, Object[] children) {
        super.treeStructureChanged(path, indices, children);
    }

    @Override
    public void treeNodesInserted(TreePath path, int[] indices, Object[] children) {
        super.treeNodesInserted(path, indices, children);
    }

    @Override
    public void treeNodesRemoved(TreePath path, int[] indices, Object[] children) {
        super.treeNodesRemoved(path, indices, children);
    }
}
