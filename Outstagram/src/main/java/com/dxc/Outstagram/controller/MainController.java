package com.dxc.Outstagram.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dxc.Outstagram.classes.JwtUtility;
import com.dxc.Outstagram.classes.Login;
import com.dxc.Outstagram.entity.Image;
import com.dxc.Outstagram.entity.Post;
import com.dxc.Outstagram.entity.User;
import com.dxc.Outstagram.model.JwtRequest;
import com.dxc.Outstagram.model.JwtResponse;
import com.dxc.Outstagram.services.AccountServices;
import com.dxc.Outstagram.services.OutstagramServices;
import com.dxc.Outstagram.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/")
@CrossOrigin(origins="*")
public class MainController {
	
	@Autowired
	private OutstagramServices os;
	
	@Autowired
	private JwtUtility jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	

	
	// API: User sign up
	@PostMapping("/signup")
	public void createUser(@RequestBody User user) {
		System.out.println("sign up called");
		os.createUser(user);
	}
	
	// API: User login v2
	@PostMapping("/login")
	public JwtResponse login(@RequestBody JwtRequest jwtRequest) throws Exception {
	
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
//			System.out.println(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword())));
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
		
		// check what role
		// get user name, use username to get account, get account type
		String role = os.getRoleFromUsername(userDetails.getUsername());
		
		final String token = jwtUtil.generateToken(userDetails, role);
		System.out.println(token);
		return new JwtResponse(token);
		
	}

	
	// Post-related controller
	
	// API: Get all Post
	@GetMapping("/post/all")
	public ArrayList<Post> getAllPost() {
		return os.getAllPost();
	}
	
	// API: Get all Post by user
	@GetMapping("/post/user")
	public ArrayList<Post> getAllPostByUser(@RequestHeader("authorization") String jwt) {
		System.out.println("called");
		return os.getAllPostByUser(jwt);
	}
	
	// API: Get Post by ID
	@GetMapping("/post/{id}")
	public Post getPostById(@PathVariable("id") String id) {
		return os.getPostById(Integer.parseInt(id));
	}
	
	
	// Filepath to save uploaded content
	@Value("${file.upload-dir}")
//	String FILE_DIRECTORY;
	String file_directory;
	// API: Add an IMAGE Post
	// Need to refactor
	@PostMapping("/post/add-image")
	public ResponseEntity<Object> addImage(@RequestParam(value ="image") MultipartFile image, @ModelAttribute Post post,  @RequestHeader("authorization") String jwt) throws IOException {
		System.out.println("this is the post" + post);
		// uploading image to local system
		// add a service
		File newPost = new File(file_directory+"\\"+image.getOriginalFilename());
		newPost.createNewFile();
		FileOutputStream fos = new FileOutputStream(newPost);
		fos.write(image.getBytes());
		fos.close();
		System.out.println("image uploaded to local system");
		
		// adding post
		String postDir = file_directory+"\\"+image.getOriginalFilename();
		// clean the header string to get only the token
		String token = jwt.substring(7);
		// get username from token
		String username = jwtUtil.getUsernameFromToken(token);
		// add image
		os.addImage(username, post, postDir);
		
		return new ResponseEntity<Object>("The File Uploaded successfully", HttpStatus.OK);
	}
	
	
//	@PostMapping("/post/add-video")
//	public ResponseEntity<Object> addVideo() {
//		return new ResponseEntity<Object>("The file uploaded", HttpStatus.OK);
//	}
	
	// API: Delete a Post
	@DeleteMapping("/post/delete/{id}")
	public ResponseEntity<String> deletePostByUser(@PathVariable("id") int id, @RequestHeader("authorization") String jwt) {
		os.deletePost(jwt, id);
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
	
	// API: Display IMAGE Post
	@GetMapping(value ="/getimage/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
	public String getImage(@PathVariable("id") int id) throws IOException {
		return os.getImage(id);
	}
	
	// API: Update Post
	@PutMapping("/post/update/{id}")
	public ResponseEntity<Object> updatePost(@PathVariable("id") int postId, @RequestBody Post post, @RequestHeader("authorization") String jwt) {
		os.updatePostById(jwt, postId, post);
		
		System.out.println("Caption updated");
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}
	
	
	// API: Add Count to Post
	@GetMapping("/post/addview/{id}")
	public void updatePostView(@PathVariable("id") int id) {
		os.addOnePostView(id);
	}
	
	
	
	// API: Check if Post is liked
	@GetMapping("/post/{id}/likes")
	public Map<String, Boolean> getPostLikes(@PathVariable("id") int postId, @RequestHeader("authorization") String jwt) {
		// if 1 == liked, if 0 == not liked
		int x = os.checkIfPostIsLiked(jwt, postId);
		
		if (x == 1) {
			return Collections.singletonMap("success", true);
		} else if(x == 0) {
			return Collections.singletonMap("success", false);
		}
		return Collections.singletonMap("success", false);
	}
	
	// API: Like a post
	@PostMapping("/post/like")
	public void likePost(@RequestBody String body, @RequestHeader("authorization") String jwt) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(body, Map.class); // map string to object
		System.out.println(map.get("postId"));
		int postId = (int) map.get("postId");
		os.likePost(jwt, postId);
	}
	
	
}

// send multiple image
//@GetMapping(value ="/getimages", produces = MediaType.IMAGE_JPEG_VALUE)
//public ResponseEntity<ArrayList<Image>> getImages() throws IOException {
//	Image image = new Image(Files.readAllBytes(Paths.get(
//			"C:\\Users\\ylim42\\outstagram-uploads\\bank.webp")));
//	Image image2 = new Image(Files.readAllBytes(Paths.get(
//			"C:\\Users\\ylim42\\outstagram-uploads\\dad.jpg")));
//	System.out.println(image);
//	System.out.println(image2);
//	ArrayList<Image> l= new ArrayList<Image>();
//	l.add(image);
//	l.add(image2);
//	System.out.println(l);
//	return new ResponseEntity<ArrayList<Image>>(l, HttpStatus.OK);
//}

// idea by wenhan => send as object => read the object in frontend, auto converted to base64???

// get single image ex
//@GetMapping(value ="/getimage", produces = MediaType.IMAGE_JPEG_VALUE)
//public ResponseEntity<ByteArrayResource> getImage() throws IOException {
//	ByteArrayResource image = new ByteArrayResource(Files.readAllBytes(Paths.get(
//			"C:\\Users\\ylim42\\outstagram-uploads\\bank.webp")));
//	System.out.println(image);
//	return ResponseEntity.status(HttpStatus.OK)
//			.contentLength(image.contentLength())
//			.body(image);
//	
//}

// testing (workingggg?!?@?)
//@GetMapping(value ="/getimage", produces = MediaType.IMAGE_JPEG_VALUE)
//public String getImage() throws IOException {
//	byte[] image = Files.readAllBytes(Paths.get("C:\\Users\\ylim42\\outstagram-uploads\\bank.webp"));
//
//	String encodedImage = Base64.getEncoder().encodeToString(image);
//	System.out.println(encodedImage);
//	return encodedImage;
//	
//	
////	String s = new String(image, StandardCharsets.UTF_8);
////	System.out.println(s);
////	return s;
//	
//	
////	ArrayList<String> al = new ArrayList<String>();
////	al.add(image);
////	return al;
//	
//}





// User login v1
//@PostMapping("/login")
//public void login(@RequestBody String user) {
//	ObjectMapper mapper = new ObjectMapper();
//	try {
//		Login lg = mapper.readValue(user, Login.class);
//		os.validateLogin(lg);
//		System.out.println(os.validateLogin(lg));
//	} catch (JsonMappingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} catch (JsonProcessingException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	
//	System.out.println("called");
//} 



// Add a Post
//@PostMapping("/post/add")
//public void addPost(@RequestParam(value="username") String username) {
//	System.out.println(username);
//	os.addImage(username);
//}


//// idea by wenhan => send as object => read the object in frontend, auto converted to base64
//@GetMapping(value ="/getimage2",  produces = MediaType.IMAGE_JPEG_VALUE)
//public ResponseEntity<ByteArrayResource> getImage2(@RequestParam(value = "path") String path) throws IOException {
//	ByteArrayResource image = new ByteArrayResource(Files.readAllBytes(Paths.get(path)));
//	System.out.println(image);
//	return ResponseEntity.status(HttpStatus.OK)
//			.contentLength(image.contentLength())
//			.body(image); 	
//}
