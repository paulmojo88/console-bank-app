package org.example.service;

import org.example.service.impl.AccountServiceImpl;
import org.example.service.impl.UserServiceImpl;

public class ServiceFactory {
    private static ServiceFactory serviceInstance;

    private ServiceFactory() {}

    public static ServiceFactory getInstance() {
        if (serviceInstance == null) {
            serviceInstance = new ServiceFactory();
        }
        return serviceInstance;
    }

    public AccountService getAccountService() {
        return new AccountServiceImpl();
    }

    public UserService getUserService() {
        return new UserServiceImpl();
    }
}
