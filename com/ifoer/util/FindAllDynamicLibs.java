package com.ifoer.util;

import android.util.Log;
import com.ifoer.dbentity.Car;
import com.ifoer.dbentity.CarLogo;
import com.ifoer.dbentity.Language;
import com.ifoer.dbentity.SerialNumber;
import com.ifoer.dbentity.Version;
import com.ifoer.entity.Cx;
import com.ifoer.entity.Versis;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FindAllDynamicLibs {
    private static final boolean f1308D = true;

    public static List<SerialNumber> getAllCarInfo(String SerialNopath, String serialNo1) {
        List<SerialNumber> result = new ArrayList();
        for (SerialNumber serialNum : getSerialNumInfo(SerialNopath, serialNo1)) {
            String serialNo = serialNum.getSerialNum();
            List<String> chexing = getCheXing(serialNum.getCarPath());
            SerialNumber sn = new SerialNumber();
            sn.setSerialNum(serialNo);
            List<Car> cars = new ArrayList();
            if (chexing != null) {
                for (int i = 0; i < chexing.size(); i++) {
                    Car car = new Car();
                    List<Version> ver = new ArrayList();
                    List<CarLogo> carLogos = new ArrayList();
                    String chexings = ((String) chexing.get(i)).toString();
                    Cx cxs = getVersion(serialNum.getCarPath() + chexings);
                    car.setName(chexings);
                    List<Versis> version = cxs.getVersion();
                    if (version != null) {
                        for (int j = 0; j < version.size(); j++) {
                            List<Language> lan = new ArrayList();
                            Version vers = new Version();
                            CarLogo carLogo = new CarLogo();
                            String versions = ((Versis) version.get(j)).getVersiones();
                            vers.setVersionName(versions);
                            carLogo.setCarLogoPath(cxs.getImagepath());
                            vers.setVersionPath(serialNum.getCarPath() + chexings + FilePathGenerator.ANDROID_DIR_SEP + versions);
                            List<String> language = getLanguage(serialNum.getCarPath() + chexings + FilePathGenerator.ANDROID_DIR_SEP + versions);
                            if (language != null) {
                                for (int k = 0; k < language.size(); k++) {
                                    Language lans = new Language();
                                    lans.setLanName(((String) language.get(k)).toString());
                                    lan.add(lans);
                                }
                            }
                            carLogos.add(carLogo);
                            vers.setLanguage(lan);
                            ver.add(vers);
                        }
                        car.setCarLogos(carLogos);
                        car.setVersions(ver);
                    }
                    cars.add(car);
                }
            }
            sn.setCarList(cars);
            result.add(sn);
        }
        return result;
    }

    private static List<SerialNumber> getSerialNumInfo(String SerialNopath, String serailNo) {
        List<SerialNumber> list = new ArrayList();
        if (SerialNopath != null) {
            File file = new File(SerialNopath);
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    for (File info : files) {
                        String fileName = info.getName();
                        if (info.isDirectory() && !fileName.equalsIgnoreCase("zipFile") && fileName.length() == 3) {
                            SerialNumber serialNum = new SerialNumber();
                            serialNum.setSerialNum(serailNo);
                            Log.i("getSerialNumInfo", "getSerialNumInfo\u6dfb\u52a0\u5e8f\u5217\u53f7" + serailNo);
                            serialNum.setCarPath(info.getAbsolutePath() + "/DIAGNOSTIC/VEHICLES/");
                            list.add(serialNum);
                        }
                    }
                }
            }
        }
        return list;
    }

    private static List<String> getCheXing(String path) {
        List<String> chexing = new ArrayList();
        if (path != null) {
            File mfile = new File(path);
            if (mfile.exists()) {
                File[] files = mfile.listFiles();
                if (files.length > 0) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            chexing.add(file.getName());
                        }
                    }
                }
            }
        }
        return chexing;
    }

    private static List<String> getLanguage(String path) {
        List<String> language = new ArrayList();
        if (path != null) {
            File mfile = new File(path);
            if (mfile.exists()) {
                File[] files = mfile.listFiles();
                if (files.length > 0) {
                    for (File file : files) {
                        if (file.getName().contains("INI_")) {
                            language.add(file.getName().substring(4, file.getName().length()));
                        }
                    }
                }
            }
        }
        return language;
    }

    private static Cx getVersion(String path) {
        Cx cx = new Cx();
        if (path != null) {
            File mfile = new File(path);
            if (mfile.exists()) {
                File[] files = mfile.listFiles();
                if (files.length > 0) {
                    List<Versis> version = new ArrayList();
                    for (File file : files) {
                        if (!file.isDirectory()) {
                            try {
                                if (file.getPath().contains(Util.PHOTO_DEFAULT_EXT) || file.getPath().contains(".jpeg") || file.getPath().contains(".bmp") || file.getPath().contains(".png") || file.getPath().contains(".gif")) {
                                    cx.setImagepath(file.getPath());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (file.getName().substring(0, 1).equalsIgnoreCase("V")) {
                            Versis Versis = new Versis();
                            Versis.setVersiones(file.getName());
                            version.add(Versis);
                        }
                    }
                    cx.setVersion(version);
                }
            }
        }
        return cx;
    }
}
