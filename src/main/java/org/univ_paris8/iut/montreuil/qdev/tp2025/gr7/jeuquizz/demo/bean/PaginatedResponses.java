package org.univ_paris8.iut.montreuil.qdev.tp2025.gr7.jeuquizz.demo.bean;

import java.util.List;

public class PaginatedResponses<T> {
    private List<T> items;
    private int currentPage;
    private int totalPages;

    public PaginatedResponses(List<T> annonces, int currentPage, int totalPages) {
        this.items = annonces;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public List<T> getItems() { return items; }
    public int getCurrentPage() { return currentPage; }
    public int getTotalPages() { return totalPages; }
}
