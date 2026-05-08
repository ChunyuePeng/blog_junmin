package org.example.junmin.dto;

public class RequestDTO<Q,D> {
    private Q q;
    private D d;
    private Page page;
    public Q getQ() {
        return q;
    }
    public void setQ(Q q) {
        this.q = q;
    }
    public D getD() {
        return d;
    }
    public void setD(D d) {
        this.d = d;
    }

    public Page getPage() {
        if (page == null) {
            page = new Page();
        }
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
