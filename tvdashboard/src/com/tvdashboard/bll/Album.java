package com.tvdashboard.bll;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {
	
	public List<Picture> pics;
	public String albumName;
	
	void Album(){
		pics = new List<Picture>() {
			
			@Override
			public <T> T[] toArray(T[] arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object[] toArray() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Picture> subList(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int size() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Picture set(int arg0, Picture arg1) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean retainAll(Collection<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean removeAll(Collection<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean remove(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Picture remove(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ListIterator<Picture> listIterator(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ListIterator<Picture> listIterator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int lastIndexOf(Object arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Iterator<Picture> iterator() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isEmpty() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int indexOf(Object arg0) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Picture get(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean containsAll(Collection<?> arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean contains(Object arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void clear() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean addAll(int arg0, Collection<? extends Picture> arg1) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean addAll(Collection<? extends Picture> arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void add(int arg0, Picture arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean add(Picture arg0) {
				// TODO Auto-generated method stub
				return false;
			}
		};
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int arg1) {
		// TODO Auto-generated method stub
		parcel.writeList(pics);
		parcel.writeString(albumName);
		
	}
}
