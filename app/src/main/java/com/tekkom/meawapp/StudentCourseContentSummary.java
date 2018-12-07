package com.tekkom.meawapp;

public class StudentCourseContentSummary {
    private String namaMateri, deskripsi, image, fileURL;

    public StudentCourseContentSummary() {
    }

    public StudentCourseContentSummary(String namaMateri, String image, String deskripsi, String fileURL) {
        this.namaMateri = namaMateri;
        this.deskripsi = deskripsi;
        this.image = image;
        this.fileURL = fileURL;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getNamaMateri() {
        return namaMateri;
    }

    public void setNamaMateri(String namaMateri) {
        this.namaMateri = namaMateri;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
