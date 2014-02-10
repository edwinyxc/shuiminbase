package com.shuimin.base.struc.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.shuimin.base.S;
import com.shuimin.base.struc.NullIterator;

class TreeNode<T> implements Iterable<TreeNode<T>>
{
	private final Map<String, Object> attrs = new TreeMap<String, Object>();

	public Object attr(String key)
	{
		return attrs.get(key);
	}

	public void attr(String key, Object v)
	{
		this.attrs.put(key, v);
	}

	private String name;

	private T value;

	private Collection<TreeNode<T>> children = new LinkedList<TreeNode<T>>();

	private TreeNode<T> parent;

	protected Selector<TreeNode<T>> selector = new Selector<TreeNode<T>>() {

		@Override
		public TreeNode<T> select(String name)
		{
			for (TreeNode<T> node : TreeNode.this) {
				if (node == null)
					continue;
				if (S.str.notBlank(node.name())
					&& node.name().equals(name)) {
					return node;
				}
			}
			return null;
		}

	};

	public String[] path()
	{
		List<String> names = new ArrayList<String>();
		
		TreeNode<?> node = this;
		
		while (node != null && node.name != "vroot") {
			names.add(0, node.name);
			node = node.parent;
		}
		
		return names.toArray(new String[0]);
	}

	protected final TreeNode<T> select(String name)
	{
		return selector.select(name);
	}

	/*
	 * protected ,so no one but Tree can build these nodes
	 */
	protected TreeNode(String name, T val)
	{
		this.value = val;
		this.name = name;
	}

	// protected TreeNode(String name, T val, TreeNode<T> parent)
	// {
	// this.value = val;
	// this.name = name;
	// appendTo(parent);
	// it = children.iterator();
	// }

	public void addAll(TreeNode<T> node)
	{
		for (TreeNode<T> nn : node.children) {
			nn.appendTo(this);
		}
	}

	/**
	 * clear the children
	 */
	public void empty()
	{
		this.children.clear();
	}

	/**
	 * set the value of this tree node
	 * 
	 * @param val
	 */
	public void value(T val)
	{
		this.value = val;
	}

	/**
	 * get the value of this tree node
	 * 
	 * @return
	 */
	public T value()
	{
		return this.value;
	}

	/**
	 * append the underlying node to the children
	 * 
	 * @param node
	 * @return
	 */
	public TreeNode<T> append(TreeNode<T> node)
	{
		node.parent = this;
		this.children.add(node);
		// node.absoluteAddress = this.absoluteAddress + "/" +
		// node.toString();
		return this;
	}

	/**
	 * append the underlying node to the children
	 * 
	 * @param node
	 * @return
	 */
	public TreeNode<T> append(String name, T value)
	{
		TreeNode<T> node = new TreeNode<T>(name, value);
		return append(node);
	}

	/**
	 * append this to the underlying`s children
	 * 
	 * @param node
	 * @return
	 */
	public TreeNode<T> appendTo(TreeNode<T> node)
	{
		S._assert(node, "node null");
		this.parent = node;
		this.parent.append(this);
		return this;
	}

	/**
	 * get name act as index
	 * 
	 * @return name
	 */
	public String name()
	{
		return name;
	}

	/**
	 * set name act as index
	 * 
	 * @param name
	 */
	public void name(String name)
	{
		this.name = name;
	}

	/**
	 * get the children values as a collection
	 * 
	 * @return
	 */
	public Collection<T> childrenValues()
	{
		List<T> list = new ArrayList<T>();
		for (TreeNode<T> n : children) {
			list.add(n.value);
		}
		return list;
	}

	/**
	 * get the children nodes as a collection
	 * 
	 * @return
	 */
	public Collection<TreeNode<T>> children()
	{
		return children;
	}

	/**
	 * @see Collection
	 * @param t
	 * @return
	 */
	public boolean contains(T t)
	{
		for (TreeNode<T> node : children) {
			if (node.value.equals(t))
				return true;
		}
		return false;
	}

	/**
	 * if this is a leaf node, measured with children`s size
	 * 
	 * @return
	 */
	public boolean isLeaf()
	{
		if (this.children.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * get parent node
	 * 
	 * @return
	 */
	public TreeNode<T> parent()
	{
		return parent;
	}

	@Override
	public Iterator<TreeNode<T>> iterator()

	{
		if (isLeaf()) {
			return new NullIterator<TreeNode<T>>();
		}
		return children.iterator();
	}
	
	public Tree<T> makeTree(){
		return new Tree<T>().root(this);
	}

	@Override
	public String toString()
	{
		String val = "";
		val = "(name:" + name + ",value:";
		if (value != null) {
			val += value.toString();
		}
		val += ",parent:";
		if (parent != null) {
			val += parent.toString();
		}
		val += ")";
		return val;
	}
	 
}
