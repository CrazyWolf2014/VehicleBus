package com.ifoer.util;

import android.os.Environment;
import android.util.Log;
import com.ifoer.dbentity.Car;
import com.ifoer.dbentity.CarLogo;
import com.ifoer.dbentity.Language;
import com.ifoer.dbentity.SerialNumber;
import com.ifoer.dbentity.Version;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindFiles {
    private static boolean f1309D;

    static {
        f1309D = false;
    }

    public static synchronized SerialNumber getFilePathFromSD(String path, String carId, int areaId, String serialNo) {
        SerialNumber serialNum;
        synchronized (FindFiles.class) {
            serialNum = new SerialNumber();
            serialNum.setSerialNum(serialNo);
            List<Car> list = new ArrayList();
            Car car = new Car();
            car.setName(carId);
            car.setAreaId(areaId);
            List<Version> versions = new ArrayList();
            List<CarLogo> carLogos = new ArrayList();
            if (Environment.getExternalStorageState().equals("mounted")) {
                if (path != null) {
                    File file = new File(path);
                    Log.i("getFilePathFromSD", "path" + path);
                    if (file.exists()) {
                        File[] files = file.listFiles();
                        if (files.length > 0) {
                            for (File file2 : files) {
                                String fileName = file2.getName();
                                String lanName;
                                if (file2.isDirectory()) {
                                    if (f1309D) {
                                        System.out.println("\u6587\u4ef6\u540d===" + file2.getName());
                                    }
                                    List<Language> languages = new ArrayList();
                                    Version version = new Version();
                                    version.setVersionName(fileName);
                                    Log.i("getFilePathFromSD", "car version path" + file2.getAbsolutePath());
                                    version.setVersionPath(file2.getAbsolutePath());
                                    File lanFile = new File(file2.getAbsolutePath());
                                    if (lanFile.exists()) {
                                        for (File lan : lanFile.listFiles()) {
                                            if (lan.isFile() && lan.getName().contains("INI_")) {
                                                lanName = lan.getName().split("_")[1];
                                                if (f1309D) {
                                                    System.out.println("\u8bed\u8a00\u6587\u4ef6\u540d==" + lanName);
                                                }
                                                Language language = new Language();
                                                language.setLanName(lanName);
                                                languages.add(language);
                                            }
                                        }
                                        version.setLanguage(languages);
                                        versions.add(version);
                                    }
                                } else {
                                    int index = fileName.lastIndexOf(".");
                                    if (index > 0) {
                                        String suffix = fileName.substring(index, fileName.length());
                                        if (!suffix.toLowerCase().equals(Util.PHOTO_DEFAULT_EXT)) {
                                            if (!suffix.toLowerCase().equals(".jpeg")) {
                                                if (!suffix.toLowerCase().equals(".bmp")) {
                                                    if (!suffix.toLowerCase().equals(".png")) {
                                                        if (!suffix.toLowerCase().equals(".gif")) {
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        lanName = null;
                                        if (fileName.contains("_")) {
                                            lanName = fileName.split("_")[1];
                                        }
                                        CarLogo carLogo = new CarLogo();
                                        carLogo.setCarLogoPath(file2.getAbsolutePath());
                                        carLogo.setLanName(lanName);
                                        carLogos.add(carLogo);
                                    }
                                }
                            }
                            car.setCarLogos(carLogos);
                            car.setVersions(versions);
                        } else if (f1309D) {
                            System.out.println("\u6ca1\u6709\u6587\u4ef6");
                        }
                    }
                }
            } else if (f1309D) {
                System.out.println("\u6ca1\u6709sd\u5361");
            }
            list.add(car);
            serialNum.setCarList(list);
        }
        return serialNum;
    }
}
