package assignment3.api;

import assignment3.model.Model;

public interface Query {

    String execute();
    void setDataSource(Model model);
}
