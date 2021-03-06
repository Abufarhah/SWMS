package edu.birzeit.swms.exceptions;

public class ResourceAssignedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private String resourceName;
    private int resourceId;
    private String assignedResourceName;
    private int assignedResourceId;

    public ResourceAssignedException() {
        super();
    }

    public ResourceAssignedException(String resourceName, int resourceId, String assignedResourceName, int assignedResourceId) {
        super(String.format("%s with id: '%s' is assigned to %s with id: %s", assignedResourceName, assignedResourceId, resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = resourceId;
        this.assignedResourceName = assignedResourceName;
        this.assignedResourceId = assignedResourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getAssignedResourceName() {
        return assignedResourceName;
    }

    public void setAssignedResourceName(String assignedResourceName) {
        this.assignedResourceName = assignedResourceName;
    }

    public int getAssignedResourceId() {
        return assignedResourceId;
    }

    public void setAssignedResourceId(int assignedResourceId) {
        this.assignedResourceId = assignedResourceId;
    }

    @Override
    public String toString() {
        return super.getMessage();
    }

}
