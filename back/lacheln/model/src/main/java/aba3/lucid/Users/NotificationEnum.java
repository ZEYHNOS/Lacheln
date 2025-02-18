package aba3.lucid.Users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationEnum {
    Y("YES"),
    N("NO");

    private final String notificationStatus;
}
