package com.tekkom.meawapp;

public class Book {
    private String IDMateri;
    private String namaMateri;
    private String deskripsi;
    private String bookURL;
    private String coverURL;
    private String uploaderID;

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    private String expiration;
    private int numOfSongs;
    private int thumbnail;

    public String getIDMateri() {
        return IDMateri;
    }

    public void setIDMateri(String IDMateri) {
        this.IDMateri = IDMateri;
    }


    public String getNamaMateri() {
        return namaMateri;
    }

    public void setNamaMateri(String namaMateri) {
        this.namaMateri = namaMateri;
    }


    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }


    public String getBookURL() {
        return bookURL;
    }

    public void setBookURL(String bookURL) {
        this.bookURL = bookURL;
    }


    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }


    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }


    public Book() {
    }

    public Book(String IDMateri, String namaMateri, String deskripsi, String bookURL, String coverURL, String uploaderID, String expiration) {
        this.IDMateri = IDMateri;
        this.namaMateri = namaMateri;
        this.deskripsi = deskripsi;
        this.bookURL = bookURL;
        this.coverURL = coverURL;
        this.uploaderID = uploaderID;
        this.expiration = expiration;

    }

    public Book(String namaMateri, int numOfSongs, int thumbnail) {
        this.namaMateri = namaMateri;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }


    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }


}