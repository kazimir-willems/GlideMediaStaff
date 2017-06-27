package delivery.com.model;

import java.io.Serializable;

/**
 * Created by Caesar on 6/20/2017.
 */

public class IssueItem implements Serializable{
    private String issueID;
    private String issueName;

    public IssueItem() {
        issueID = "";
        issueName = "";
    }

    public IssueItem(String id, String name) {
        this.issueID = id;
        this.issueName = name;
    }

    public void setIssueID(String value) {
        this.issueID = value;
    }

    public String getIssueID() {
        return issueID;
    }

    public void setIssueName(String value) {
        this.issueName = value;
    }

    public String getIssueName() {
        return issueName;
    }

    @Override
    public String toString() {
        return issueName;
    }
}
