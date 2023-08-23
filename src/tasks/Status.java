package tasks;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    private String status;

    private void GetStatus(String status) {
        this.status = status;
    }

    public String GetStatus() {
        return status;
    }

    public static Status getEnum(String status) {

        switch (status) {
            case "NEW":
                return NEW;
            case "IN_PROGRESS":
                return IN_PROGRESS;
            case "DONE":
                return DONE;
            default:
                return null;
        }
    }
}

