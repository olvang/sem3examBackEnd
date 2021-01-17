package rest;

import errorhandling.LoginInvalidExceptionMapper;
import errorhandling.UsernameTakenExceptionMapper;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(cors.CorsFilter.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        resources.add(security.JWTAuthenticationFilter.class);
        resources.add(security.LoginEndpoint.class);
        resources.add(security.RolesAllowedFilter.class);
        resources.add(security.errorhandling.AuthenticationExceptionMapper.class);
        resources.add(security.errorhandling.NotAuthorizedExceptionMapper.class);

        //resources
        resources.add(rest.DemoResource.class);
        resources.add(rest.UserResource.class);
        resources.add(rest.SportResource.class);
        resources.add(rest.SportTeamResource.class);
        resources.add(rest.CoachResource.class);
        resources.add(rest.PlayerResource.class);

        //Exceptions
        resources.add(LoginInvalidExceptionMapper.class);
        resources.add(UsernameTakenExceptionMapper.class);
        resources.add(errorhandling.GenericExceptionMapper.class);
        resources.add(errorhandling.MissingInputExceptionMapper.class);
        resources.add(errorhandling.AlreadyExistExceptionMapper.class);
        resources.add(errorhandling.NotFoundExceptionMapper.class);
    }
    
}
