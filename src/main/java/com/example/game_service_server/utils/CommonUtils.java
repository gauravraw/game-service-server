package com.example.game_service_server.utils;

import com.example.game_service_server.enums.ErrorCode;
import com.example.game_service_server.exception.GameServiceException;
import com.example.game_service_server.non_entity.request.PlayerScore;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@UtilityClass
@Slf4j
public class CommonUtils {

    public static final Set<String> ALLOWED_EXTENSIONS = Set.of("txt", "csv");

    public static List<PlayerScore> readFile(MultipartFile multipartFile) {
        try {
            InputStream inputStream = multipartFile.getInputStream();

            String line;

            List<PlayerScore> dataList = new ArrayList<>();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            // Skipping header
            bufferedReader.readLine();

            // File format -> Sl.No,player_id,player_name,score

            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                dataList.add(PlayerScore.builder().playerId(Integer.parseInt(data[1])).score(Integer.parseInt(data[3]))
                        .playerName(data[2]).build());
            }

            log.info("Read file successfully");

            return dataList;
        } catch (IOException ioException) {
            log.error("Error occurred while reading file ", ioException);
            throw new GameServiceException(ErrorCode.UNABLE_TO_READ_FILE);
        }
    }

    public static void validateFile(MultipartFile multipartFile) {
        // TODO :: check file with validations

    }
}
