package com.vdtt.data;

public class SQLStatement {
    public static final String INSERT_USED_GIFT_CODE = "INSERT INTO `gift_code_histories`(`player_id`,`user_id`, `gift_code`,`type_code`, `updated_at`) VALUES (?, ?, ?, ?, ?)";
    public static final String CHECK_EXIST_USED_GIFT_CODE = "SELECT * FROM `gift_code_histories` WHERE `gift_code` = ? AND (`player_id` = ? OR `user_id` = ?) LIMIT 1;";
    public static final String CHECK_EXIST_TYPE_GIFT_CODE = "SELECT * FROM `gift_codes` INNER JOIN `gift_code_histories` ON `gift_codes`.`code` = `gift_code_histories`.`gift_code` WHERE `gift_codes`.`giftcode_event` = ? AND (`gift_code_histories`.`player_id` = ? OR `gift_code_histories`.`user_id` = ?) LIMIT 1;";
    public static final String GET_GIFT_CODE = "SELECT * FROM `gift_codes` WHERE `code` = ? AND (expires_at IS NULL OR expires_at > now()) LIMIT 1;";
    public static final String CHECK_EXIST_TYPE = "SELECT * FROM `gift_code_histories` WHERE (`player_id` = ? OR `user_id` = ?) AND `type_code` = ? LIMIT 1;";

    public static final String ACTIVE_ACCOUNT = "UPDATE `users` SET `activated` = ? WHERE `username` = ? LIMIT 1;";
    public static final String ACTIVE_ACCOUNT2 = "UPDATE `users` SET `activated_2` = ? WHERE `username` = ? LIMIT 1;";
  //  public static final String LOCK_ACCOUNT = "UPDATE `users` SET `status` = ? WHERE `username` = ? LIMIT 1;";
  //  public static final String BANUNI_ACCOUNT = "UPDATE `users` SET `ban_until` = ? WHERE `username` = ? LIMIT 1;";

    public static final String GET_COIN = "SELECT `coin` FROM `users` WHERE `username` = ? LIMIT 1;";
}
