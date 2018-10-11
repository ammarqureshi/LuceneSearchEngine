package com.lucene.document;

public class CranfieldDocument {

	public String id;
	public String title;
	public String authors;
	public String bibliog;
	public String words;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getBibliog() {
		return bibliog;
	}
	public void setBibliog(String bibliog) {
		this.bibliog = bibliog;
	}
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	@Override
	public String toString() {
		return "CranfieldDocument [id=" + id + ", \ntitle=" + title + ", \nauthors=" + authors + ", \nbibliog=" + bibliog
				+ ", \n words=" + words + "]";
	}
	

}
