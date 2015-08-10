package kr.pe.junho85.domain.model.command;

public enum TistoryVisibility {
    PRIVATE(0), PROTECT(1), PUBLIC(2), PUBLISH(3);
    private int value;

    TistoryVisibility(int value) {
        this.value = value;
    }
};

