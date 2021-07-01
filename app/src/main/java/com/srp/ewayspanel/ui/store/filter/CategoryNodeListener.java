package com.srp.ewayspanel.ui.store.filter;

import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;

/**
 * Created by Eskafi on 11/16/2019.
 */
public interface CategoryNodeListener {
    void selectedNode(CategoryTreeNode selectedNode);

    void selectedNodeRootParent(Long rootParent);
}
