package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.UpdateHistory;
import com.demo.campingnavi.repository.jpa.UpdateHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    CampService campService;

    @Override
    public String recommendModelUpdate() {
        String pyFile = "ModelTraining.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            result = "success";
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }

        return result;
    }

    @Override
    public String campDataUpdate() {
        String pyFile = "campingData.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            result = "success";
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }
        return result;
    }

    @Override
    public int getCampingTotalCount() {
        String pyFile = "campingTotalCount.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        int result = -1;
        try {
            Process process = processBuilder.start();
            process.waitFor();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            result = Integer.parseInt(reader.readLine());
            System.out.println(pyFile + " 실행 성공");
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
        }
        return result;
    }

    @Override
    public String getCampingDataFromApi(int page) {
        String pyFile = "campingDataSearch.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, String.valueOf(page));
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            String filename = "temp/tmp_campingData" + page + ".csv";
            filename = PathConfig.realPath(filename);
            File file = new File(filename);
            if (file.exists()) {
                result = "success";
            } else {
                result = "fail";
            }
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }
        return result;
    }

    @Override
    public String getCampingDataIntegration(int totalPage) {
        String pyFile = "campingDataIntegration.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, String.valueOf(totalPage));
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            result = "success";
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }
        return result;
    }

    @Override
    public Map<String, Integer> getCrawlingStatus() {
        Map<String, Integer> crawlingStatus = new HashMap<>();
        int current = 0;
        int total = campService.getCampListByUseyn("").size();
        while (true) {
            if (current >= total) {
                clearRatingTempFile(total+1);
                break;
            }
            String filename = "temp/rating" + (current+1) + ".csv";
            filename = PathConfig.realPath(filename);
            File file = new File(filename);
            if (!file.exists()) {
                break;
            }
            current++;
        }

        crawlingStatus.put("current", current);
        crawlingStatus.put("total", total);
        return crawlingStatus;
    }

    @Override
    public String getCrawlingData() {
        String pyFile = "CrawlingDataSearch.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            result = reader.readLine();
            reader.close();
            inputStream.close();
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }

        return result;
    }


    @Override
    public String getCrawlingDataIntegration() {
        String pyFile = "CrawlingDataIntegration.py";
        pyFile = PathConfig.realPath(pyFile);

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile);
        String result = "";
        try {
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println(pyFile + " 실행 성공");
            result = "success";
        } catch (IOException | InterruptedException e) {
            System.out.println(pyFile + " 실행 실패");
            result = "fail";
        }

        return result;
    }

    @Override
    public void clearRatingTempFile(int start_number) {
        int i = start_number;
        while (true) {
            String filename = "temp/rating" + i + ".csv";
            filename = PathConfig.realPath(filename);
            File file = new File(filename);
            if (!file.exists()) {
                break;
            } else {
                file.delete();
            }
            i++;
        }

        String filename = "temp/crawling_status.csv";
        filename = PathConfig.realPath(filename);
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}
