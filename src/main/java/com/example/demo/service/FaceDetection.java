//package com.example.demo.service;
//
//import org.bytedeco.opencv.opencv_core.*;
//import org.bytedeco.opencv.opencv_face.*;
//import org.opencv.objdetect.CascadeClassifier;
//
//import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
//
//
//public class FaceDetection {
//
//    public static void main(String[] args) {
//        String image1Path = "path/to/person1.jpg";
//        String image2Path = "path/to/person2.jpg";
//
//        // Load the images
//        Mat image1 = imread(image1Path);
//        Mat image2 = imread(image2Path);
//
//        // Load Haarcascades for face detection
//        CascadeClassifier faceDetector = new CascadeClassifier();
//        faceDetector.load("path/to/haarcascade_frontalface_default.xml");
//
//        // Detect faces in the images
//        RectVector faceRects1 = new RectVector();
//        RectVector faceRects2 = new RectVector();
//        faceDetector.detectMultiScale(image1, faceRects1);
//        faceDetector.detectMultiScale(image2, faceRects2);
//
//        // Initialize face recognizer
//        FaceRecognizer faceRecognizer = FisherFaceRecognizer.create();
//
//        // Train the recognizer (similar to previous example)
//        // ...
//
//        // Perform face recognition
//        int predictedLabel1 = -1;
//        int predictedLabel2 = -1;
//
//        if (!faceRects1.empty()) {
//            Mat faceImage1 = new Mat(image1, faceRects1.get(0));
//            predictedLabel1 = faceRecognizer.predict(faceImage1);
//        }
//
//        if (!faceRects2.empty()) {
//            Mat faceImage2 = new Mat(image2, faceRects2.get(0));
//            predictedLabel2 = faceRecognizer.predict(faceImage2);
//        }
//
//        // Compare the two images
//        if (predictedLabel1 != -1 && predictedLabel2 != -1 && predictedLabel1 == predictedLabel2) {
//            System.out.println("These two images are likely of the same person.");
//        } else {
//            System.out.println("These two images are likely of different people.");
//        }
//    }
//}
