package com.tvdashboard.tmdb.wrapper.collections;

import java.util.List;

import com.tvdashboard.tmdb.wrapper.movie.Genre;

public class GenreList implements java.io.Serializable {
	/**
	 * Class <code>GenreList</code>
	 */
	private static final long serialVersionUID = 2975654010431776211L;
	private List<Genre> genres;

	public GenreList() {
	}

	public List<Genre> getGenres() {
		return this.genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}
}
