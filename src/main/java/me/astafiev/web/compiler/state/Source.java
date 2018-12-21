/*
 * Авторское право принадлежит Антону Александровичу Астафьеву <anton@astafiev.me> (Anton Astafiev) ѱ.
 * Все права защищены и охраняются законом.
 * Copyright (c) 2018 Антон Александрович Астафьев <anton@astafiev.me> (Anton Astafiev). All rights reserved.
 * 
 *  Собственная лицензия Астафьева
 * Данный программный код является собственностью Астафьева Антона Александровича
 * и может быть использован только с его личного разрешения
 */

package me.astafiev.web.compiler.state;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 *
 * @author Антон Астафьев <anton@astafiev.me> (Anton Astafiev)
 */
public class Source {
	private static final int BUNDLE_AVG_SIZE = 10;

	private final String name;
	private NavigableSet<Bundle> bundles;
	private int lastLineIndex = 0;
	private final Comparator<Bundle> comparator = (lb, rb) -> lb.getIndex() - rb.getIndex();
	private Date date = new Date();
	private String wholeText;

	public Source(String name) {
		bundles = new TreeSet<>(comparator);
		this.name = name;
	}

	public Source(String name, String text) {
		setSource(text);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public final void setSource(String text) {
		List<String> lines = Arrays.asList(text.split("\n"));
		int bundleQty = lines.size() / BUNDLE_AVG_SIZE;
		bundles = new TreeSet<>(comparator);
		lastLineIndex = 0;
		for (int i=0; i< bundleQty; i++ ) {
			Bundle bundle = new Bundle(lastLineIndex);
			bundle.addAll(lines.subList(lastLineIndex, lastLineIndex + BUNDLE_AVG_SIZE));
			addBundle(bundle);
		}
		{
			Bundle bundle = new Bundle(lastLineIndex);
			bundle.addAll(lines.subList(lastLineIndex, lines.size()));
			addBundle(bundle);
		}
		wholeText = text;
	}

	public String get(int index) {
		Bundle h = findLocation(index);
		return h.get(index);
	}

	public void insert(int index, String line) {
		Bundle h = findLocation(index);
		h.insert(index, line);
		onChanged();
		bundles.tailSet(h, false).forEach(Bundle::increase);
	}

	public String replace(int index, String line) {
		Bundle h = findLocation(index);
		onChanged();
		return h.set(index, line);
	}

	public String remove(int index) {
		Bundle h = findLocation(index);
		String rem = h.remove(index);
		bundles.tailSet(h, false).forEach(Bundle::decrease);
		onChanged();
		return rem;
	}

	private void onChanged() {
		wholeText = null;
		date = new Date();
	}
	
	public Date getDate() {
		return date;
	}

	public String getТекст() {
		if (wholeText == null) {
			wholeText = bundles.stream().flatMap(b -> b.toStream()).reduce("", (l,r)->l+"\n"+r) + "\n";
		}
		return wholeText;
	}

	Bundle findLocation(int index) {
		Bundle res = bundles.headSet(new Bundle(index), true).last();
		return res;
	}

	private void addBundle(Bundle bundle) {
		bundles.add(bundle);
		lastLineIndex += bundle.size();
	}

}
