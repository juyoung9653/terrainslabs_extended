package com.terrainslabs2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class Terrainslabs2Config {

    private static final Gson GSON = new GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    private static final String FILE_NAME = "terrainslabs2.json";
    private static Terrainslabs2Config instance;

    @Expose
    public int dirtDepth = 1;

    // true: 잔디반블록 + 바닐라 잔디블록 아래 흙도 변환
    @Expose
    public boolean convertDirtUnderGrassBlock = true;

    // true: 흙 아래가 돌(stone)이면 변환하지 않음
    @Expose
    public boolean skipDirtOnStone = true;

    // y좌표 탐색 최소값. 이 값 미만은 스캔하지 않음 (성능 최적화)
    @Expose
    public int minScanY = 60;

    private Terrainslabs2Config() {}

    public static Terrainslabs2Config load() {
        if (instance != null) return instance;

        Path path = FabricLoader.getInstance()
            .getConfigDir()
            .resolve(FILE_NAME);

        if (Files.exists(path)) {
            try {
                String json = Files.readString(path);
                instance = GSON.fromJson(json, Terrainslabs2Config.class);
                Terrainslabs2Mod.LOGGER.info("Loaded config: {}", json);
            } catch (IOException e) {
                Terrainslabs2Mod.LOGGER.error(
                    "Failed to load config, using defaults",
                    e
                );
                instance = new Terrainslabs2Config();
            }
        } else {
            instance = new Terrainslabs2Config();
        }

        // 항상 저장해서 새 필드 추가 시 반영
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(instance));
        } catch (IOException e) {
            Terrainslabs2Mod.LOGGER.error("Failed to save config", e);
        }

        return instance;
    }
}
