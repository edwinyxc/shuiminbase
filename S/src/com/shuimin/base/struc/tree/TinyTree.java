package com.shuimin.base.struc.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

import com.shuimin.base.S;
import com.shuimin.base.f.F;
import com.shuimin.base.f.F2;
import com.shuimin.base.f.FArray;
import com.shuimin.base.f.For;

public class TinyTree<E> implements Tree<E> {
	public TinyTree(E root) {
		this.elem = root;
		this.parent = null;
		this.root = this;
		name(root.toString());
	}

	private TinyTree(Tree<E> tree) {
		this(tree.elem());
		this.children.addAll(tree.children());
	}

	public TinyTree() {
	}

	@SuppressWarnings("rawtypes")
	public final static Tree emptyTree() {
		return new TinyTree();
	}

	private final Map<String, Object> attrs = new TreeMap<String, Object>();

	private int idxInParent = -1;

	private E elem;

	private List<Tree<E>> children = new LinkedList<Tree<E>>();

	private Tree<E> parent;

	private Tree<E> root;

	protected Selector<Tree<E>> selector = new Selector<Tree<E>>() {
		@Override
		public Tree<E> select(String name) {
			for (Tree<E> node : TinyTree.this.children) {
				if (node == null)
					continue;
				if (S.str.notBlank(node.name()) && node.name().equals(name)) {
					return node;
				}
			}
			return null;
		}

	};

	public String[] path() {
		return new FArray<String>(
				new For(parents()).map(new F<String, Tree<E>>() {
					@Override
					public String f(Tree<E> a) {
						return a.name();
					}
				})).toArray(new String[0]);
	}

	public Tree<E> select(String name) {
		return selector.select(name);
	}

	@Override
	public Tree<E> root() {
		return root;
	}

	@Override
	public Tree<E> add(Tree<E> t) {
		t.addTo(this);
		return this;
	}

	@Override
	public List<Tree<E>> children() {
		return children;
	}

	@Override
	public Tree<E> parent() {
		return parent;
	}

	@Override
	public List<Tree<E>> parents() {
		final List<Tree<E>> ret = new ArrayList<Tree<E>>();
		Tree<E> node = this;
		while (node != null) {
			ret.add(node);
			node = node.parent();
		}
		return ret;
	}

	@Override
	public Tree<E> find(String name) {
		return null;
	}

	@Override
	public Tree<E> last() {
		return children.get(children.size() - 1);
	}

	@Override
	public Tree<E> first() {
		return children.get(0);
	}

	@Override
	public Tree<E> prev() {
		return siblings().get(idxInParent - 1);
	}

	@Override
	public List<Tree<E>> prevAll() {
		return new FArray<Tree<E>>(parent.children()).slice(0, idxInParent);
	}

	@Override
	public Tree<E> before(Tree<E> t) {
		siblings().add(idxInParent, t);
		return this;
	}

	@Override
	public Tree<E> next() {
		return siblings().get(idxInParent + 1);
	}

	@Override
	public Tree<E> after(Tree<E> t) {
		siblings().add(idxInParent + 1, t);
		return this;
	}

	@Override
	public List<Tree<E>> nextAll() {
		return new FArray<Tree<E>>(parent.children()).slice(idxInParent + 1,
				parent.children().size());
	}

	@Override
	public boolean isLeaf() {
		return children.size() == 0;
	}

	@Override
	public boolean isRoot() {
		return parent == null;
	}

	@Override
	public void remove() {
		siblings().remove(this);
	}

	@Override
	public Tree<E> remove(Tree<E> t) {
		children.remove(t);
		return this;
	}

	@Override
	public Tree<E> remove(Iterable<Tree<E>> t) {

		for (Tree<E> _t : t) {
			children.remove(_t);
		}
		return this;
	}

	@Override
	public String name() {
		return (String) this.attr("name");
	}

	@Override
	public Tree<E> name(String name) {
		return this.attr("name", name);
	}

	@Override
	public Tree<E> find(F<Boolean, E> findFunc) {
		return null;
	}

	// @Override
	// public Iterator<Tree<E>> iterator() {
	// if (isLeaf()) {
	// return new NullIterator<Tree<E>>();
	// }
	// return children.iterator();
	// }

	@Override
	public Tree<E> asNew() {
		return new TinyTree<E>(this);
	}

	@Override
	public E elem() {
		return this.elem;
	}

	@Override
	public Tree<E> elem(E t) {
		this.elem = t;
		return this;
	}

	@Override
	public Tree<E> addTo(Tree<E> t) {
		root = t.root();
		parent = t.parent();
		this.idxInParent = t.children().size();
		t.children().add(this);
		return this;
	}

	@Override
	public Tree<E> attr(String name, Object o) {
		this.attrs.put(name, o);
		return this;
	}

	@Override
	public Object attr(String name) {
		return this.attrs.get(name);
	}

	private class DFS implements Iterator<Tree<E>> {
		private Stack<Iterator<Tree<E>>> stack = new Stack<Iterator<Tree<E>>>();

		public DFS(Tree<E> node) {
			S._assert(node, "node null");
			stack.push(node.children().iterator());
		}

		@Override
		public boolean hasNext() {
			if (stack.isEmpty()) {
				return false;
			}
			Iterator<Tree<E>> it = stack.peek();
			if (it.hasNext()) {
				return true;
			}
			stack.pop();
			return hasNext();
		}

		@Override
		public Tree<E> next() {
			if (hasNext()) {
				Iterator<Tree<E>> it = stack.peek();
				Tree<E> next = it.next();
				if (!next.isLeaf()) {
					stack.push(next.children().iterator());
				}
				return next;
			}
			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"remove not supported, yet.");
		}
	}

	private class BFS implements Iterator<Tree<E>> {

		private Queue<Iterator<Tree<E>>> queue = new LinkedList<Iterator<Tree<E>>>();

		public BFS(Tree<E> node) {
			queue.offer(node.children().iterator());
		}

		@Override
		public boolean hasNext() {
			if (queue.isEmpty()) {
				return false;
			}
			Iterator<Tree<E>> it = queue.peek();
			if (it.hasNext()) {
				return true;
			}
			queue.poll();
			return hasNext();
		}

		@Override
		public Tree<E> next() {
			if (hasNext()) {
				Iterator<Tree<E>> it = queue.peek();
				Tree<E> next = it.next();
				if (!next.isLeaf()) {
					queue.offer(next.children().iterator());
				}
				return next;
			}
			return null;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(
					"remove not supported, yet.");
		}

	}

	@Override
	public Iterator<Tree<E>> bfs() {
		return new BFS(this);
	}

	@Override
	public Iterator<Tree<E>> dfs() {
		return new DFS(this);
	}

	// ***test

	public static void main(String[] args) {
		Tree<String> tree = new TinyTree<String>("root");
		tree.add(new TinyTree<String>("1")
				.add(new TinyTree<String>("2").add(new TinyTree<String>("5"))
						.add(new TinyTree<String>("6")))
				.add(new TinyTree<String>("3")).add(new TinyTree<String>("4")));
		Iterator<Tree<String>> it = tree.bfs();
		while (it.hasNext()) {
			System.out.println(it.next().elem());
		}
	}

	@Override
	public List<Tree<E>> siblings() {
		if (parent == null)
			return Collections.emptyList();
		return parent.children();
	}

	@Override
	public Tree<E> select(String[] name) {
		Tree<E> searchResult = this;
		int i = 0;
		while (i < name.length) {
			searchResult = new F2<Tree<E>, Tree<E>, String>() {

				@Override
				public Tree<E> f(Tree<E> node, String name) {
					S._assert(node, "node null");
					Selector<Tree<E>> selector = node.selector();
					S._assert(selector, "selector null");
					Tree<E> _node = selector.select(name);
					return _node;
				}

			}.f(searchResult, (String) name[i]);

			if (searchResult == null) {
				return null;
			}
			i++;
		}
		if (searchResult == this)
			return null;
		return searchResult;
	}

	@Override
	public Selector<Tree<E>> selector() {
		return selector;
	}
}
