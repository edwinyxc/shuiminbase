package com.shuimin.base.struc.tree;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

import com.shuimin.base.S;

public class LinkedTree<T> extends AbstractCollection<TreeNode<T>> {

	private TreeNode<T> vroot = new TreeNode<T>("vroot", null);// default

	public TreeNode<T> vroot() {
		return vroot;
	}

	public final static int DFS = 0;

	public final static int BFS = 1;

	private int mode = BFS;

	protected int size;

	public LinkedTree(String name, T t) {
		this.vroot.append(new TreeNode<T>(name, t));
	}

	public TreeNode<T> newNode(String name, T val) {
		return new TreeNode<T>(name, val);
	}

	public LinkedTree() {
	}

	public LinkedTree(TreeNode<T> node) {
		S._assert(node, "node null");
		this.vroot.append(node);
	}

	public LinkedTree<T> root(TreeNode<T> node) {
		S._assert(node, "node null");
		this.vroot.append(node);
		return this;
	}

	public TreeNode<T> root() {
		for (TreeNode<T> n : vroot.children()) {
			return n;
		}
		return null;
	}

	public void setIteratorMode(int i) {
		this.mode = i;
		size = 0;
	}

	@Override
	public Iterator<TreeNode<T>> iterator() {
		if (mode == BFS)
			return new BFSIterator<T>(vroot);
		return new DFSIterator<T>(vroot);
	}

	@Override
	public int size() {
		throw new RuntimeException("size not avaliable");
	}

	/**
	 * level by level
	 * 
	 * @param names
	 * @return
	 */
	private TreeNode<T> _getStrict(Object[] names) {
		TreeNode<T> searchResult = vroot();
		int i = 0;
		while (i < names.length) {
			searchResult = __searchInNode(searchResult, (String) names[i]);
			if (searchResult == null) {
				return null;
			}
			i++;
		}
		if (searchResult == vroot)
			return null;
		return searchResult;
	}

	private TreeNode<T> __searchInNode(TreeNode<T> node, String name) {
		S._assert(node, "node null");
		Selector<TreeNode<T>> selector = node.selector;
		S._assert(selector, "selector null");
		TreeNode<T> _node = selector.select(name);
		return _node;
	}

	@SuppressWarnings("unchecked")
	private Tree<T>[] _getBFSfuzzy(String[] names) {
		int modeForRecover = mode;
		mode = BFS;
		ArrayList<Tree<T>> list = new ArrayList<Tree<T>>();
		try {
			for (TreeNode<T> s : this) {
				if (-1 != S.array.contains(names, s.name())) {
					list.add(s.makeTree());
				}
			}
		} finally {
			mode = modeForRecover;
		}
		return (Tree<T>[]) S.array.fromList(Tree.class, list);
	}

	public Tree<T> select(String selector) {
		return _getStrict(_resolvePathSelect(selector)).makeTree();
	}

	public Tree<T>[] find(String query) {
		return _getBFSfuzzy(query.split(" "));
	}

	private String[] _resolvePathSelect(String names) {
		String[] name_a = names.split("/");
		if (name_a == null || name_a.length < 1)
			return new String[0];
		if (S.str.isBlank(name_a[0]))
			name_a[0] = "/";
		name_a = (String[]) S.array.shrink(name_a);
		return name_a;
	}

	public String debug() {
		StringBuilder tree = new StringBuilder();
		int tmp_mode = this.mode;
		setIteratorMode(BFS);
		tree.append("RT BFS DEBUG:");
		for (TreeNode<T> n : this) {
			tree.append("\n->");
			tree.append(n);
		}
		this.mode = tmp_mode;
		return tree.toString();
	}

}
