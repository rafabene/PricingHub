package com.rafabene.infocadastrais;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@LoginConfig(authMethod = "MP-JWT")
@ApplicationScoped
public class AtivosApplication extends Application{
    
}
