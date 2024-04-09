package com.example.dao;

import com.example.model.Menu;
import com.example.util.HibernateSessionFactory;

import java.util.List;

public class MenuDAO {
    public List<Menu> getMenu() {
        return (List<Menu>) HibernateSessionFactory
                .getSessionFactory()
                .openSession()
                .createQuery("From Menu")
                .getResultList();
    }
}
