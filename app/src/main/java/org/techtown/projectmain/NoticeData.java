package org.techtown.projectmain;

public class NoticeData {
    private String noticeId;
    private String noticeContents;
    private String noticeDate;
    private String noticePojectInfo;
    private String noticeKind;

    public NoticeData(String noticeId, String noticeContents, String noticeDate, String noticePojectInfo, String noticeKind){
        this.noticeId = noticeId;
        this.noticeContents = noticeContents;
        this.noticeDate = noticeDate;
        this.noticePojectInfo = noticePojectInfo;
        this.noticeKind = noticeKind;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeContents() {
        return noticeContents;
    }

    public void setNoticeContents(String noticeContents) {
        this.noticeContents = noticeContents;
    }

    public String getNoticeDate() {
        return noticeDate;
    }

    public void setNoticeDate(String noticeDate) {
        this.noticeDate = noticeDate;
    }

    public String getNoticePojectInfo() {
        return noticePojectInfo;
    }

    public void setNoticePojectInfo(String noticePojectInfo) {
        this.noticePojectInfo = noticePojectInfo;
    }

    public String getNoticeKind() {
        return noticeKind;
    }

    public void setNoticeKind(String noticeKind) {
        this.noticeKind = noticeKind;
    }



}
