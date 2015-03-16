package ae.ac.adec.coursefollowup.services;

import ae.ac.adec.coursefollowup.Application.myApplication;

/**
 * Created by Tareq on 03/12/2015.
 */
public class BusinessRoleError extends Exception {

    public BusinessRoleError(String Message){
        super(Message);
    }
    public BusinessRoleError(int ResourceID){
        super(getName(ResourceID));
    }
    private static String getName(int ResourceID) {
        String name = myApplication.getContext().getResources().getString(ResourceID);
        return name;
    }
}
