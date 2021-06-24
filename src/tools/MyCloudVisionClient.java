//package tools;// Imports the Google Cloud client library
//
//import com.google.cloud.vision.v1.AnnotateImageRequest;
//import com.google.cloud.vision.v1.AnnotateImageResponse;
//import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
//import com.google.cloud.vision.v1.EntityAnnotation;
//import com.google.cloud.vision.v1.Feature;
//import com.google.cloud.vision.v1.Feature.Type;
//import com.google.cloud.vision.v1.Image;
//import com.google.cloud.vision.v1.ImageAnnotatorClient;
//import com.google.protobuf.ByteString;
//import setting.Setting;
//
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyCloudVisionClient {
//	public static void request(byte[] bytes) throws Exception {
//		// Initialize client that will be used to send requests. This client only needs to be created
//		// once, and can be reused for multiple requests. After completing all of your requests, call
//		// the "close" method on the client to safely clean up any remaining background resources.
//		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
//
//			// The path to the image file to annotate
//			String fileName = Setting.get("google_cloud_vision_request_image");
//
//			// Reads the image file into memory
////			Path path = Paths.get(fileName);
////			byte[] data = Files.readAllBytes(path);
//			ByteString imgBytes = ByteString.copyFrom(bytes);
//
//			// Builds the image annotation request
//			List<AnnotateImageRequest> requests = new ArrayList<>();
//			Image img = Image.newBuilder().setContent(imgBytes).build();
////			Image img = Image.newBuilder().
//			Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
//			AnnotateImageRequest request =
//					AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
//			requests.add(request);
//
//			// Performs label detection on the image file
//			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
//			List<AnnotateImageResponse> responses = response.getResponsesList();
//
//			for (AnnotateImageResponse res : responses) {
//				if (res.hasError()) {
//					System.out.format("Error: %s%n", res.getError().getMessage());
//					return;
//				}
//
//				for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//					System.out.println(annotation.getDescription());
//				}
//			}
//			System.out.println("vision called");
//		}
//	}
//}