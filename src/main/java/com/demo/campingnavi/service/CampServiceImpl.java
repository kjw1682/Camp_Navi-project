package com.demo.campingnavi.service;

import com.demo.campingnavi.config.PathConfig;
import com.demo.campingnavi.domain.Camp;
import com.demo.campingnavi.domain.Member;
import com.demo.campingnavi.dto.CampRecommendVo;
import com.demo.campingnavi.dto.CampVo;
import com.demo.campingnavi.repository.jpa.CampRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CampServiceImpl implements CampService {
    @Autowired
    private CampRepository campRepo;
    @Autowired
    private DataService dataService;

    @Override
    public Camp getCampByCseq(int cseq) {
        return campRepo.findById(cseq).get();
    }

    @Override
    public CampVo getCampVoByCseq(int cseq, Member member) {
        List<Camp> campList = new ArrayList<>();
        campList.add(getCampByCseq(cseq));
        CampRecommendVo campRecommendVo = new CampRecommendVo();
        saveCampRecommendList(campList, member, campRecommendVo);
        return campRecommendVo.getCampRecommendListAll().get(0);
    }

    @Override
    public List<Camp> getCampScanList(CampRecommendVo campRecommendVo) {
        String useyn = campRecommendVo.getUseyn();
        if (useyn.equals("a")) {
            useyn = "";
        }

        String name = "";
        String locationB = "";
        String locationS = "";
        String searchField = campRecommendVo.getSearchField();
        String searchWord = campRecommendVo.getSearchWord();
        if (searchField.equals("name")) {
            name = searchWord;
        } else if (searchField.equals("locationB")) {
            if (searchWord.length() == 2) {
                String tmp = campRecommendVo.getAddrMatch().get(searchWord);
                if (tmp != null) {
                    searchWord = tmp;
                }
            }
            locationB = searchWord;
        } else if (searchField.equals("locationS")) {
            locationS = searchWord;
        }

        String[] campType = campRecommendVo.getCampType();
        int tmp = 0;
        for (String s : campType) {
            tmp += s.length();
        }
        if (tmp == 0) {
            for (int i = 0 ; i < campType.length ; i++) {
                campType[i] = campRecommendVo.getCampTypeArray()[i][0];
            }
            campRecommendVo.setCampType(campType);
        }

        String[] campTypeParams = new String[campType.length];
        for (int i = 0 ; i < campType.length ; i++) {
            String type = campRecommendVo.getCampType()[i];
            if (!type.equals("")) {
                campTypeParams[i] = campRecommendVo.getCampTypeArray()[i][1];
            } else {
                campTypeParams[i] = "|";
            }
        }

        String sortBy = campRecommendVo.getSortBy();
        String sortDirection = campRecommendVo.getSortDirection();

        List<Camp> campList = campRepo.getCampList(useyn, name, locationB, locationS,
               campTypeParams[0], campTypeParams[1], campTypeParams[2], campTypeParams[3]);

        return campList;
    }

    @Override
    public void saveCampRecommendList(List<Camp> filteredList, Member member, CampRecommendVo campRecommendVo) {
        String pyFile = "Recommend.py";
        String csvFile = "tmp_filtered.csv";
        pyFile = PathConfig.realPath(pyFile);
        dataService.campListOutToCsv(filteredList, csvFile, "");
        csvFile = PathConfig.realPath(csvFile);

        List<CampVo> campRecommendListNotVisited = new ArrayList<>();
        List<CampVo> campRecommendListVisited = new ArrayList<>();
        List<CampVo> campRecommendListAll = new ArrayList<>();

        ProcessBuilder processBuilder = new ProcessBuilder("python", pyFile, String.valueOf(member.getMseq()));
        try {
            Process process = processBuilder.start();
            System.out.println("파이썬 프로그램 실행 성공!");
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파이썬 프로그램 실행 실패!");
        }

        File file = new File(csvFile);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println(csvFile+"삭제 완료");
            } else {
                System.out.println(csvFile+"삭제 실패");
            }
        }

        String recommendFile = "recommend.csv";
        recommendFile = PathConfig.realPath(recommendFile);
        file = new File(recommendFile);
        if (file.exists()) {
            System.out.println(recommendFile + " 생성 성공");

            // 받아오기 프로세스 입력
            int check = -1;
            int count = 0;
            String text = "";

            try {
                FileReader fr = new FileReader(recommendFile);
                BufferedReader br = new BufferedReader(fr);

                while(true) {
                    text = br.readLine();
                    if (text == null) {
                        System.out.println("-------text is null!-------");
                        break;
                    }

                    if (check != -1) {
                        String[] input = text.split(",");
                        Camp camp = getCampByCseq(Integer.parseInt(input[0]));
                        CampVo campVo = new CampVo(camp, Float.parseFloat(input[1]), input[2]);

                        campRecommendListAll.add(campVo);
                        if (input[2].equals("y")) {
                            campRecommendListNotVisited.add(campVo);
                        } else {
                            campRecommendListVisited.add(campVo);
                        }

                        text = "";
                    } else {
                        check = 0;
                        text = "";
                    }
                }

                br.close();
                fr.close();

                file = new File(recommendFile);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println(recommendFile + " 삭제 완료");
                    } else {
                        System.out.println(recommendFile + " 삭제 실패");
                    }
                }
                campRecommendVo.setCampRecommendListNotVisited(campRecommendListNotVisited);
                campRecommendVo.setCampRecommendListVisited(campRecommendListVisited);
                campRecommendVo.setCampRecommendListAll(campRecommendListAll);

            } catch (IOException e) {
                System.out.println((count+1)+"번 데이터 입력 중 오류 발생!");
                e.printStackTrace();
            }
        } else {
            System.out.println(recommendFile + " 생성 실패");
        }

    }

    @Override
    public List<Camp> searchItems(String keyword) {
        return campRepo.findByNameContaining(keyword);
    }

    @Override
    public void campAllDisabled() {
//        List<Camp> campList = getCampListByUseyn("y");
//        for (Camp camp : campList) {
//            camp.setUseyn("n");
//            campRepo.save(camp);
//        }
        campRepo.campAllDisabled();
    }

    @Override
    public List<Camp> getCampListByUseyn(String useyn) {
        return campRepo.findByUseynContaining(useyn);
    }

    @Override
    public List<Camp> searchCamps(String keyword) {
        return campRepo.searchCamps(keyword);
    }

    @Override
    public Camp getCampByName(String campName) {
        return campRepo.findCampByName(campName);
    }

    @Override
    public boolean isValidCampName(String campName) {
        Camp camp = campRepo.findCampByName(campName);
        return camp != null;
    }



}
