package ru.abstractcoder.murdermystery.core.config;

import ru.abstractcoder.benioapi.config.msg.MsgKey;
import ru.abstractcoder.benioapi.config.msg.MsgProperties;

public enum Messages implements MsgKey {

    general__you_leave_arena("&cВы покинули арену"),
//    general__leaved,
    general__joined_broadcast("&7{player} &eприсоединился к игре &a({current}/{max})"),
    general__not_enough_coins("&cНедостаточно монеток для этого!"),
//    general__,

    lobby__waiting_bossbar("&c&lMurderMystery &6&lProstocraft"),
    lobby__starting_bossbar("&fДо старта осталось &6{time_left}"),
    lobby__starting_chat("&fИгра начнется через &6{time_left}"),
    lobby__starting_title(
            "&eОсталось:",
            "&a{time_left}"
    ),
    lobby__waiting_sidebar_state("&fОжидание игроков..."),
    lobby__starting_sidebar_state("§fСтарт: §6{time_left}"),

    game__murder_on_the_prow("&cУбийца вышел на охоту"),
    game__murder_get_money_for_kill("&fТы получил &a{amount} монет &fза убийство"),
    game__survivor_get_money_periodic("&fТы получил &a{amount} монет &fза {period} выживания"),
    game__murder_and_detective_chance("&eШанс стать убийцей: &a{detective_chance}%&e, детективом: &a{murder_chance}%"),
    game__you_die("“&cТы был убит"),
    game__wrong_killer_die("&cВы ошиблись убийцей и поплатились за это жизнью"),

    game__august_muzir_cooldown("&cВы можете двигаться только через &a{time}"),
    game__miss_purple_murder_near("Убийца близко... Я чувствую..."),
    game__el_termu_camera_can_placed_only_on_wall("&cКамеры можно устанавливать только на стенах"),
    game__detective_die_chat("Детектив умер. Лук утерян."),
    game__detective_die_title_survivors("&6Детектив умер", "&eНайдите лук и остановите убийцу!"),
    game__detective_die_title_murder("&6Детектив умер"),
    game__detective_bow_picked_up("&fЛук детектива был поднят."),
    game__doctor_revive_cooldown("&fСледующий труп Вы сможете оживить только через &a{time}"),
    game__doctor_spectator_leaved("&cЭтот игрок уже вышел из игры"),
    game__prostitute_sex_cooldown("&fСекс будет доступен через &a{time}"),
    game__prostitute_role_info("&fВо время бурного секса вы порвали целку и узнали роль этого игрока: &e{role}"),
    game__,
    game__role_selecting_animation("&fВыбор роли: &a{random_role}"),
    game__role_animation_end("&cВы {role}"),
//    game__,

    misc__alive("Жив"),
    misc__dead("Мёртв"),
    misc__spectator("Наблюдатель"),
    misc__survivor_detector_compass("&eДетектор выживших"),
    misc__bow_detector_compass("&eДетектор лука"),
    misc__slowing_snowballs("&fСнежки замедления"),
    //    misc__,

    gui__class_already_purchased("&cЭтот класс уже куплен"),
    gui__class_succ_purchased("&aВы успешно купили этот класс"),

    private final MsgProperties properties;

    Messages(MsgProperties properties) {
        this.properties = properties;
    }

    Messages(String... lines) {
        this(MsgProperties.create(lines));
    }

    @Override
    public MsgProperties getProperties() {
        return properties;
    }

    public static Messages lifeState(boolean alive) {
        return alive ? misc__alive : misc__dead;
    }

}