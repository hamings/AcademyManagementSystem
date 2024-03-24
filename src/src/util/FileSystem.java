package src.util;

import src.ServiceType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileSystem {
    private static Map<ServiceType, String> filenameMap;
    private static Map<ServiceType, Boolean> isNotSavedMap;
    private static Map<ServiceType, Map> mapCache;
    static {
        isNotSavedMap = new HashMap<>();
        isNotSavedMap.put(ServiceType.LECTURE, false);
        isNotSavedMap.put(ServiceType.STUDENT, false);
        isNotSavedMap.put(ServiceType.TEACHER, false);
        isNotSavedMap.put(ServiceType.LECTUREREGISTRATION, false);
        isNotSavedMap.put(ServiceType.NOTIFICATION, false);

        filenameMap = new HashMap<>();
        filenameMap.put(ServiceType.LECTURE, "lecture.txt");
        filenameMap.put(ServiceType.STUDENT, "student.txt");
        filenameMap.put(ServiceType.TEACHER, "teacher.txt");
        filenameMap.put(ServiceType.LECTUREREGISTRATION, "lectureregistration.txt");
        filenameMap.put(ServiceType.NOTIFICATION, "notification.txt");

        try {
            makeFile(ServiceType.TEACHER);
            makeFile(ServiceType.STUDENT);
            makeFile(ServiceType.LECTUREREGISTRATION);
            makeFile(ServiceType.LECTURE);
            makeFile(ServiceType.NOTIFICATION);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mapCache = new HashMap<>();
    }
    public static void makeFile(ServiceType serviceType) throws IOException {
        String filename = filenameMap.get(serviceType);
        File file = new File(filename);
        file.createNewFile();
    }

    public static Map loadObjectMap(ServiceType serviceType) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;
        Map objectMap = null;
        String filename = filenameMap.get(serviceType);


        try{
            File file = new File(filename);

            if(file.length() == 0) return new HashMap();

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            ois = new ObjectInputStream(bis);
            if(isNotSavedMap.get(serviceType)) objectMap = mapCache.get(serviceType);
            else {
                //System.out.println(1111);
                objectMap = (Map) ois.readObject();
                isNotSavedMap.put(serviceType, true);

                mapCache.put(serviceType, objectMap);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(ois != null) {
                    ois.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        return objectMap;
    }

    public static void saveObjectMap(ServiceType serviceType, Map objectMap) throws IOException {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;
        String filename = filenameMap.get(serviceType);


        try{
            fos = new FileOutputStream(filename);
            bos = new BufferedOutputStream(fos);
            oos = new ObjectOutputStream(bos);

            oos.writeObject(objectMap);
            //isNotSavedMap.put(serviceType, false);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            oos.close();
        }
    }
}
