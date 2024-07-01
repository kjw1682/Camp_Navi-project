package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Review;
import com.demo.campingnavi.repository.jpa.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private CampRepository campRepo;

    @Override
    public List<Camp> campInFromCsv(String csvFile, String n) {
        // CSV 파일에서 camp 데이터를 읽어 데이터베이스에 저장하는 메소드
        csvFile = PathConfig.realPath(csvFile);
        List<Camp> campList = new ArrayList<Camp>();
        List<String> errors = new ArrayList<String>();

        int num = -1;
        int check = -1;
        int count = 0;
        String text = "";

        if (!n.equals("all")) {
            try {
                num = Integer.parseInt(n);
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

        if (num >= 0 || n.equals("all")) {
            try {
                FileInputStream fr = new FileInputStream(csvFile);
                InputStreamReader ir = new InputStreamReader(fr, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(ir);

                while(true) {
                    if (!n.equals("all")) {
                        if (count == num) {
                            break;
                        }
                    }

                    text = br.readLine();

                    if (text == null)
                        break;

                    if (check != -1) {
                        String[] input = text.split(";");

                        Optional<Camp> op_camp = campRepo.findCampByContentId(input[0]);

                        try {
                            Camp camp = new Camp();
                            if (!op_camp.isEmpty()) {
                                camp = op_camp.get();
                            }
                            camp.setContentId(input[0]);
                            camp.setName(input[1]);
                            camp.setCreatedAt(input[2].isEmpty() ? null : LocalDate.parse(input[2]));
                            camp.setAddr1(input[3]);
                            camp.setAddr2(input[4]);
                            camp.setMapY(input[5]);
                            camp.setMapX(input[6]);
                            camp.setCampType(input[7]);
                            camp.setLocationB(input[8]);
                            camp.setLocationS(input[9]);
                            camp.setUseyn("y");
                            campRepo.save(camp);
                            if (op_camp.isEmpty()) {
                                camp = campRepo.findFirstByOrderByCseqDesc();
                            }
                            campList.add(camp);
                            count++;
                        } catch(Exception e) {
                            e.printStackTrace();
                            String tmp = "";
                            for (int i = 0 ; i < input.length ; i++) {
                                tmp += i+" : "+input[i]+"\n";
                            }
                            tmp += "\n";
                            errors.add(tmp);
                        }
                        text = "";
                    } else {
                        check = 0;
                        text = "";
                    }
                }
                br.close();
                fr.close();
            } catch (IOException e) {
                System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
                e.printStackTrace();
            }
        } else {
            System.out.println("데이터 입력 실패!");
        }

        csvFile = "tmp_camp.csv";
        String pyFile = "campListToCsv.py";
        campListOutToCsv(campList, csvFile, pyFile);

        return campList;
    }

    @Override
    public void reviewListOutToCsv(List<Review> reviewList) {
        // 리뷰가 작성될 때 Review.csv 파일로 평점정보를 내보내는 메소드
        String csvFile = "tmp_review.csv";
        String pyFile = "ReviewListToCsv.py";
        csvFile = PathConfig.realPath(csvFile);
        pyFile = PathConfig.realPath(pyFile);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("vseq,")
                .append("member,")
                .append("camp,")
                .append("rate\n");

        for (Review review : reviewList) {
            stringBuilder
                    .append(review.getVseq()).append(",")
                    .append(review.getMember().getMseq()).append(",")
                    .append(review.getCamp().getCseq()).append(",")
                    .append(review.getLikes()).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(stringBuilder.toString());
            }
            fileWriter.close();
            ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, csvFile);
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("review 데이터 내보내기 성공");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("review 데이터 내보내기 실패");
        }

        File file = new File(csvFile);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("임시파일 삭제 완료");
            } else {
                System.out.println("임시파일 삭제 실패");
            }
        }
    }

    @Override
    public void reviewListDeleteInCsv(List<Integer> reviewList) {
        // 리뷰가 작성될 때 Review.csv 파일로 평점정보를 내보내는 메소드
        String csvFile = "tmp_review.csv";
        String pyFile = "ReviewListDeleteInCsv.py";
        csvFile = PathConfig.realPath(csvFile);
        pyFile = PathConfig.realPath(pyFile);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("vseq\n");

        for (int n : reviewList) {
            stringBuilder
                    .append(n).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(stringBuilder.toString());
            }
            fileWriter.close();
            ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, csvFile);
            Process process = processBuilder.start();
            process.waitFor();
            System.out.println("review 데이터 내보내기 성공");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("review 데이터 내보내기 실패");
        }

        File file = new File(csvFile);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("임시파일 삭제 완료");
            } else {
                System.out.println("임시파일 삭제 실패");
            }
        }
    }

    @Override
    public void campListOutToCsv(List<Camp> campList, String csvFile, String pyFile) {
        // 평점정보를 가져올 캠프 리스트를 내보내는 메소드
        csvFile = PathConfig.realPath(csvFile);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("cseq;")
                .append("컨텐츠아이디;")
                .append("캠핑장이름;")
                .append("useyn\n");

        for (Camp camp : campList) {
            stringBuilder
                    .append(camp.getCseq()).append(";")
                    .append(camp.getContentId()).append(";")
                    .append(camp.getName()).append(";")
                    .append(camp.getUseyn()).append("\n");
        }

        try {
            FileWriter fileWriter = new FileWriter(csvFile);
            try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
                bufferedWriter.write(stringBuilder.toString());
            }
            fileWriter.close();
            System.out.println("캠프 데이터 내보내기 성공("+csvFile+")");
            if (!pyFile.equals("")) {
                pyFile = PathConfig.realPath(pyFile);
                ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, csvFile);
                Process process = processBuilder.start();
                process.waitFor();
                File file = new File(csvFile);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println(csvFile+"삭제 완료");
                    } else {
                        System.out.println(csvFile+"삭제 실패");
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("캠프 데이터 내보내기 실패("+csvFile+")");
        }
    }


    @Override
    public String deleteFile(String filename) {
        String result = "";
        filename = PathConfig.realPath(filename);
        File file = new File(filename);
        if (file.exists()) {
            if (file.delete()) {
                result = "success";
            } else {
                result = "fail";
            }
        } else {
            result = "not exist";
        }
        return result;
    }

    @Override
    public String createFile(String filename) {
        String result = "";
        filename = PathConfig.realPath(filename);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("");
            fileWriter.close();
            result = "success";
        } catch (Exception e) {
            result = "fail";
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public String createDir(String dir) {
        String result = "";
        dir = PathConfig.realPath(dir);
        File file = new File(dir);
        if (!file.isDirectory()) {
            try {
                if (file.mkdir()) {
                    result = "success";
                } else {
                    result = "fail";
                }
            } catch (Exception e) {
                result = "fail";
                e.printStackTrace();
            }
        } else {
            result = "success";
        }

        return result;
    }


}
