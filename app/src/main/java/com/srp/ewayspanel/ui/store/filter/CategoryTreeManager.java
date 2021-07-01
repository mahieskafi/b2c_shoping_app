package com.srp.ewayspanel.ui.store.filter;

import android.util.Pair;

import com.srp.ewayspanel.model.storepage.category.CategoryItem;
import com.srp.ewayspanel.repository.storepage.StorePageRepository;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode.BoxType;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode.SelectionState;
import com.srp.ewayspanel.repository.storepage.category.CategoryTreeNode.ToggleState;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CategoryTreeManager {

    public static final int NO_POSITION_SELECTED = -1;

    public static final int NO_PARENT_EXPANDED_POSITION = -1;
    public static final int NO_PARENTID = 0;

    private List<CategoryItem> mRawCategoryItems;

    private List<CategoryTreeNode> mVisibleTreeNodes = new ArrayList<>();

    public List<CategoryTreeNode> getNodes(){
        return mVisibleTreeNodes;
    }

    private CategoryTreeNode mSelectedNode = null;
    private List<CategoryTreeNode> mSelectionPathNodes = new ArrayList<>();

    public CategoryTreeManager(StorePageRepository.CategoryData categoryData) {
        mRawCategoryItems = categoryData.categoryRawList;

        mVisibleTreeNodes.addAll(categoryData.categoryTree.getChildren());


        resetData();
    }

    private void resetData() {

        for(CategoryTreeNode item : mVisibleTreeNodes){
            resetExpandedState(item);
            removeChildrenForNode(item);
        }

    }

    public int getCount() {
        return mVisibleTreeNodes.size();
    }

    public CategoryTreeNode getDataAt(int position) {
        if (position < 0 || (position > 0 && position > mVisibleTreeNodes.size())) {
            throw new IndexOutOfBoundsException("position = " + position);
        }

        return mVisibleTreeNodes.get(position);
    }

    /**
     * @param position
     * @return a pair object: first element represents the toggleState for categoryTreeNode in position,
     * the second element is the number of changed elements by this operation
     */
    public Pair<ToggleState, Integer> toggle(int position) {
        Pair changingState;

        ToggleState toggleState;
        int changedCount;

        CategoryTreeNode node = getDataAt(position);

        if (node.isExpanded()) {
            resetExpandedState(node);

            changedCount = removeChildrenForNode(node);

            if (isSelected(position) && node.hasChildren()) {
                removeNodesFromSelectionPath(node.getChildren());
            }

            toggleState = ToggleState.COLLAPSED;
        } else {
            if (!node.hasChildren()) {
                changedCount = 0;
            } else {
                List<CategoryTreeNode> children = new ArrayList<>(node.getChildren());
                for (CategoryTreeNode child : children) {
                    resetExpandedState(child);
                }

                mVisibleTreeNodes.addAll(position + 1, children);
                changedCount = children.size();

                if (isSelected(position) && node.hasChildren()) {
                    addNodesToSelectionPath(node.getChildren());
                }
            }

            node.setExpanded(true);
            toggleState = ToggleState.EXPANDED;
        }

        onToggleStateChanged(position, toggleState);

        changingState = new Pair<>(toggleState, changedCount);

        return changingState;
    }

    private void onToggleStateChanged(int position, ToggleState newToggleState) {
        CategoryTreeNode toggledNode = getDataAt(position);

        if (!toggledNode.isRoot()) {
            updateParentAndSiblingsBoxType(toggledNode, newToggleState);
        }

        updateChildrenBoxType(toggledNode, newToggleState);
    }

    private void updateParentAndSiblingsBoxType(CategoryTreeNode toggledNode, ToggleState parentToggleState) {
        CategoryTreeNode parent = toggledNode.getParent();

        List<CategoryTreeNode> siblings = parent.getChildren();
        int siblingCount = siblings.size();

        if (siblingCount == 1) {
            toggledNode.setBoxType(BoxType.STANDARD);

            return;
        }
        //siblingCount > 1

        int nodePositionInSiblings = 0;
        for (int i = 0; i < siblingCount; ++i) {
            if (toggledNode.getCategoryId().equals(siblings.get(i).getCategoryId())) {
                nodePositionInSiblings = i;
                break;
            }
        }

        if (parentToggleState == ToggleState.EXPANDED) {
            if (nodePositionInSiblings == 0) {
                toggledNode.setBoxType(BoxType.STANDARD);

                CategoryTreeNode nextSibling = siblings.get(nodePositionInSiblings + 1);
                CategoryTreeNode nextNextNode = null;
                if (siblingCount > 2) {
                    nextNextNode = siblings.get(nodePositionInSiblings + 2);
                }

                nextSibling.setBoxType(nextNextNode == null ? BoxType.STANDARD : (nextSibling.isExpanded() ? BoxType.STANDARD : BoxType.TOP));
            }
            else if (nodePositionInSiblings == siblingCount - 1) {
                CategoryTreeNode prevSibling = siblings.get(nodePositionInSiblings - 1);

                toggledNode.setBoxType(prevSibling.isExpanded() ? BoxType.STANDARD : BoxType.BOTTOM);
            }
            else {//there will be atLeast 3 sibling
                CategoryTreeNode prevSibling = siblings.get(nodePositionInSiblings - 1);
                CategoryTreeNode nextSibling = siblings.get(nodePositionInSiblings + 1);

                CategoryTreeNode prevPrevNode = null;
                CategoryTreeNode nextNextNode = null;

                if (nodePositionInSiblings - 2 >= 0) {
                    prevPrevNode = siblings.get(nodePositionInSiblings - 2);
                }

                if (nodePositionInSiblings + 2 < siblingCount) {
                    nextNextNode = siblings.get(nodePositionInSiblings + 2);
                }

                if (prevSibling.isExpanded()) {
                    toggledNode.setBoxType(BoxType.STANDARD);

                    prevSibling.setBoxType(prevPrevNode == null ? BoxType.STANDARD : prevPrevNode.isExpanded() ? BoxType.STANDARD : BoxType.BOTTOM);
                }
                else {
                    toggledNode.setBoxType(BoxType.BOTTOM);

                    prevSibling.setBoxType(prevPrevNode == null ? BoxType.TOP : prevPrevNode.isExpanded() ? BoxType.TOP : BoxType.MIDDLE);
                }

                nextSibling.setBoxType(nextNextNode == null ? BoxType.STANDARD : nextSibling.isExpanded() ? BoxType.STANDARD : BoxType.TOP);
            }
        }
        else {
            if (nodePositionInSiblings == 0) {
                toggledNode.setBoxType(BoxType.TOP);

                CategoryTreeNode nextSibling = siblings.get(nodePositionInSiblings + 1);

                if (siblingCount > 2) {
                    nextSibling.setBoxType(nextSibling.isExpanded() ? BoxType.BOTTOM : BoxType.MIDDLE);
                }
                else {
                    nextSibling.setBoxType(BoxType.BOTTOM);
                }
            }
            else if (nodePositionInSiblings == siblingCount - 1) {
                CategoryTreeNode prevSibling = siblings.get(nodePositionInSiblings - 1);

                if (prevSibling.isExpanded()) {
                    toggledNode.setBoxType(BoxType.STANDARD);
                }
                else {
                    if (siblingCount > 2) {
                        prevSibling.setBoxType(BoxType.MIDDLE);
                    }
                    else {
                        prevSibling.setBoxType(BoxType.TOP);
                    }

                    toggledNode.setBoxType(BoxType.BOTTOM);
                }
            }
            else {//there will be atLeast 3 sibling
                CategoryTreeNode prevSibling = siblings.get(nodePositionInSiblings - 1);
                CategoryTreeNode nextSibling = siblings.get(nodePositionInSiblings + 1);

                CategoryTreeNode prevPrevNode = null;
                CategoryTreeNode nextNextNode = null;

                if (nodePositionInSiblings - 2 >= 0) {
                    prevPrevNode = siblings.get(nodePositionInSiblings - 2);
                }

                if (nodePositionInSiblings + 2 < siblingCount) {
                    nextNextNode = siblings.get(nodePositionInSiblings + 2);
                }

                if (prevSibling.isExpanded()) {
                    toggledNode.setBoxType(BoxType.TOP);//because there is atLeast one nextSibling.

                    prevSibling.setBoxType(prevPrevNode == null ? BoxType.STANDARD : (prevPrevNode.isExpanded() ? BoxType.STANDARD : BoxType.BOTTOM));
                }
                else {
                    toggledNode.setBoxType(BoxType.MIDDLE);//because there is atLeast one nextSibling.

                    prevSibling.setBoxType(prevPrevNode == null ? BoxType.TOP : (prevPrevNode.isExpanded() ? BoxType.TOP : BoxType.MIDDLE));
                }

                nextSibling.setBoxType(nextNextNode == null ? BoxType.BOTTOM : nextSibling.isExpanded() ? BoxType.BOTTOM : BoxType.MIDDLE);

            }
        }
    }

    private void updateChildrenBoxType(CategoryTreeNode parent, ToggleState parentToggleState) {
        if (!parent.hasChildren()) {
            return;
        }

        List<CategoryTreeNode> children = parent.getChildren();
        int childCount = children.size();

        if (childCount == 1) {
            CategoryTreeNode onlyChild = children.get(0);
            onlyChild.setBoxType(BoxType.STANDARD);

            return;
        }

        setBoxTypeForNodeGroup(children);

        if (parentToggleState == ToggleState.EXPANDED) {
            //do nothing
        } else {
            setBoxTypeForNodeGroup(children);

            for (CategoryTreeNode child : children) {
                updateChildrenBoxType(child, ToggleState.COLLAPSED);
            }
        }
    }

    private void setBoxTypeForNodeGroup(List<CategoryTreeNode> siblings) {
        int childCount = siblings.size();

        for (int i = 0; i < childCount; ++i) {
            CategoryTreeNode child = siblings.get(i);

            if (i == 0) {
                child.setBoxType(BoxType.TOP);
            } else if (i == childCount - 1) {
                child.setBoxType(BoxType.BOTTOM);
            } else {
                child.setBoxType(BoxType.MIDDLE);
            }
        }
    }

    public void setSelectedNode(int position) {
        if (mSelectedNode != null) {
            mSelectedNode.setSelectionState(SelectionState.UNSELECTED);

            ListIterator<CategoryTreeNode> iterator = mSelectionPathNodes.listIterator();
            while (iterator.hasNext()) {
                iterator.next().setSelectionState(SelectionState.UNSELECTED);
                iterator.remove();
            }

            if (position == NO_POSITION_SELECTED) {
                mSelectedNode = null;

                return;
            }

            CategoryTreeNode selectingNode = getDataAt(position);
            if (selectingNode == mSelectedNode) {
                mSelectedNode = null;

                return;
            }
        }
        else if(position == NO_POSITION_SELECTED){
            return;
        }

        mSelectedNode = getDataAt(position);
        mSelectedNode.setSelectionState(SelectionState.SELECTED);

        CategoryTreeNode parent = mSelectedNode.getParent();
        while (parent != null) {
            addNodeToSelectionPath(parent);

            parent = parent.getParent();
        }

        if (mSelectedNode.isExpanded() && mSelectedNode.hasChildren()) {
            addNodesToSelectionPath(mSelectedNode.getChildren());
        }
    }

    public void setSelectedNode(long initiallySelectedCategoryId) {

        List<Long> parentsIds = findParentsIds(initiallySelectedCategoryId);

        if (parentsIds.isEmpty()) {

            for(int k = 0; k < mVisibleTreeNodes.size(); k++){
                if(mVisibleTreeNodes.get(k).getCategoryId().equals(initiallySelectedCategoryId)) {
                    setSelectedNode(k);
                    break;
                }
            }

            return;
        }

        clearSelectedRoot();

        int togglePosition = -1;

        int i = parentsIds.size() - 1;
        int parentPosition = NO_PARENT_EXPANDED_POSITION;
        CategoryTreeNode parent = null;

        do {

            if (i == parentsIds.size() - 1) {
                parentPosition = findRootParentPositionInTree(parentsIds.get(i));

                if (parentPosition == NO_PARENT_EXPANDED_POSITION) {
                    return;
                }

                togglePosition = parentPosition;

                if(!mVisibleTreeNodes.get(togglePosition).isExpanded()) {
                    toggle(togglePosition);
                }

                parent = mVisibleTreeNodes.get(parentPosition);
            } else {
                parentPosition = findParentTreeNodeForNextLevel(parent, parentsIds.get(i));

                togglePosition += (parentPosition + 1);//because positions come from listIndex, they start at 0 --> the +1 came from there!

                for(int j = 0; j < mVisibleTreeNodes.size(); j++) {

                    if(mVisibleTreeNodes.get(j).getCategoryId().equals(parentsIds.get(i))){
                        if (!mVisibleTreeNodes.get(j).isExpanded()) {
                            toggle(j);
                        }
                        break;
                    }
                }

                parent = parent.getChildren().get(parentPosition);
            }

            --i;

        } while (i >= 0);

        if (togglePosition != -1) {//last level should be selected
            for (int j = 0; j < parent.getChildren().size(); ++j) {
                CategoryTreeNode childAt = parent.getChildren().get(j);
                if (childAt.getCategoryId().equals(initiallySelectedCategoryId)) {

                    for(int k = 0; k < mVisibleTreeNodes.size(); k++){
                        if(mVisibleTreeNodes.get(k).getCategoryId().equals(initiallySelectedCategoryId)) {
                            setSelectedNode(k);
                            break;
                        }
                    }
                    break;


                }
            }
        }


//        int togglePosition = -1;
//
//        int i = parentsIds.size() - 1;
//        int parentPosition = NO_PARENT_EXPANDED_POSITION;
//        CategoryTreeNode parent = null;
//
//        do {
//
//            if (i == parentsIds.size() - 1) {
//                parentPosition = findRootParentPositionInTree(parentsIds.get(i));
//
//                if (parentPosition == NO_PARENT_EXPANDED_POSITION) {
//                    return;
//                }
//
//                togglePosition = parentPosition;
//                toggle(togglePosition);
//
//                parent = mVisibleTreeNodes.get(parentPosition);
//            } else {
//                parentPosition = findParentTreeNodeForNextLevel(parent, parentsIds.get(i));
//
//                togglePosition += (parentPosition + 1);//because positions come from listIndex, they start at 0 --> the +1 came from there!
//
//                //all parents should be just expanded and not selected
//                toggle(togglePosition);
//
//                parent = parent.getChildren().get(parentPosition);
//            }
//
//            --i;
//
//        } while (i >= 0);
//
//        if (togglePosition != -1) {//last level should be selected
//            for (int j = 0; j < parent.getChildren().size(); ++j) {
//                CategoryTreeNode childAt = parent.getChildren().get(j);
//                if (childAt.getCategoryId() == initiallySelectedCategoryId) {
//                    int selectedPosition = togglePosition + (j + 1);//because positions come from listIndex, they start at 0 --> the +1 came from there!
//
//                    setSelectedNode(selectedPosition);
//                }
//            }
//        }
    }

    private void clearSelectedRoot(){

        for(CategoryTreeNode currItem : mVisibleTreeNodes){
            resetChildren(currItem);

//            resetExpandedState(currItem);
        }
    }

    private void resetChildren(CategoryTreeNode node){
        if(node.getSelectionState() != SelectionState.UNSELECTED){
            node.setSelectionState(SelectionState.UNSELECTED);
        }

        if(node.hasChildren()) {
            for (CategoryTreeNode child : node.getChildren()) {

                if (child.getSelectionState() != SelectionState.UNSELECTED) {
                    child.setSelectionState(SelectionState.UNSELECTED);
                }

                resetChildren(child);
            }
        }
    }

    public Long getSelectedNodeRootParent(CategoryTreeNode selectedNode) {
        CategoryTreeNode rootParent = null;

        while (selectedNode.getParent() != null) {
            rootParent = selectedNode;
            selectedNode = selectedNode.getParent();
        }
        if (rootParent != null) {
            return rootParent.getCategoryId();
        } else {
            return selectedNode.getCategoryId();
        }
    }

    private BoxType getTreeNodeBoxType(int position) {
        return mVisibleTreeNodes.get(position).getBoxType();
    }

    private int findRootParentPositionInTree(long rootParentCategoryId) {
        //mVisibleTreeNodes just contains rootNodes
        for (int i = 0; i < mVisibleTreeNodes.size(); ++i) {
            if (rootParentCategoryId == mVisibleTreeNodes.get(i).getCategoryId()) {
                return i;
            }
        }

        return NO_PARENT_EXPANDED_POSITION;
    }

    private int findParentTreeNodeForNextLevel(CategoryTreeNode node, long nextLevelParentId) {
        //mVisibleTreeNodes just contains rootNodes
        for (int i = 0; i < node.getChildren().size(); ++i) {
            if (nextLevelParentId == node.getChildren().get(i).getCategoryId()) {
                return i;
            }
        }

        return NO_PARENT_EXPANDED_POSITION;
    }

    private List<Long> findParentsIds(long initiallySelectedCategoryId) {
        List<Long> parentsIds = new ArrayList<>();

        long parentId = initiallySelectedCategoryId;

        do {
            parentId = getParentId(parentId);

            if (parentId != NO_PARENTID) {
                parentsIds.add(parentId);
            }

        } while (parentId != NO_PARENTID);

        return parentsIds;
    }

    private long getParentId(long categoryId) {
        for (CategoryItem categoryItem : mRawCategoryItems) {
            if (categoryItem.getCategoryId() == categoryId) {
                return categoryItem.getParentId();
            }
        }

        return NO_PARENTID;
    }

    private void addNodesToSelectionPath(List<CategoryTreeNode> newInPathNodes) {
        for (CategoryTreeNode newNodeInSelectionPath : newInPathNodes) {
            addNodeToSelectionPath(newNodeInSelectionPath);
        }
    }

    private void addNodeToSelectionPath(CategoryTreeNode newNodeInSelectionPath) {
        newNodeInSelectionPath.setSelectionState(SelectionState.IN_SELECTIONPATH);
        mSelectionPathNodes.add(newNodeInSelectionPath);
    }

    private void removeNodesFromSelectionPath(List<CategoryTreeNode> oldNodesInPath) {
        for (CategoryTreeNode oldNodeInPath : oldNodesInPath) {
            removeNodeFromSelectionPath(oldNodeInPath);
        }
    }

    private void removeNodeFromSelectionPath(CategoryTreeNode oldNodeInSelectionPath) {
        oldNodeInSelectionPath.setSelectionState(SelectionState.UNSELECTED);
        mSelectionPathNodes.remove(oldNodeInSelectionPath);
    }

    private void resetExpandedState(CategoryTreeNode node) {
        node.setExpanded(false);

        if (!node.hasChildren()) {
            return;
        }

        for (CategoryTreeNode child : node.getChildren()) {
            resetExpandedState(child);
        }
    }

    private int removeChildrenForNode(CategoryTreeNode node) {
        int removedCount = 0;

        if (!node.hasChildren()) {
            return removedCount;
        }

        mVisibleTreeNodes.removeAll(node.getChildren());
        removedCount = node.getChildren().size();

        for (CategoryTreeNode child : node.getChildren()) {
            removedCount += removeChildrenForNode(child);
        }

        return removedCount;
    }

    public boolean isSelected(int position) {
        CategoryTreeNode node = getDataAt(position);

        if (node == mSelectedNode) {
            return true;
        }

        return false;
    }

    public ArrayList<Integer> brotherPositions(int position) {
        ArrayList<Integer> brotherPositions = new ArrayList<>();

        CategoryTreeNode currentNode = mVisibleTreeNodes.get(position);
        int currentPosition = position;
        int currentDepth = currentNode.getDepth();
        do {
            currentPosition--;
            if (currentPosition > -1) {
                currentNode = mVisibleTreeNodes.get(currentPosition);

                if (currentNode.getDepth() == currentDepth) {
                    brotherPositions.add(currentPosition);
                }
            }
        }
        while (currentNode.getDepth() > currentDepth && currentPosition > -1);


        currentNode = mVisibleTreeNodes.get(position);
        currentPosition = position;
        do {
            currentPosition++;
            if (currentPosition < getCount()) {
                currentNode = mVisibleTreeNodes.get(currentPosition);

                if (currentNode.getDepth() == currentDepth) {
                    brotherPositions.add(currentPosition);
                }
            }
        }
        while (currentNode.getDepth() >= currentDepth && currentPosition < getCount());

        return brotherPositions;
    }

}
