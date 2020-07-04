package com.appdeveloper.photoappuserapi.controller;

import com.appdeveloper.photoappuserapi.ui.model.CreateUserRequestModel;
import com.appdeveloper.photoappuserapi.ui.model.CreateUserResponseModel;
import com.appdeveloper.photoappuserapi.ui.model.UserResponseModel;
import com.appdeveloper.photoappuserapi.users.data.UserEntity;
import com.appdeveloper.photoappuserapi.users.service.UsersService;
import com.appdeveloper.photoappuserapi.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
 @Autowired
 Environment env;
 @Autowired
 UsersService usersService;
 @GetMapping("/status")
 public String getStatus(){
 return "working on port:"+env.getProperty("local.server.port")+ "with token = "+env.getProperty("token.secret");
 }

 @PostMapping(
         consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
         produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
 )
 public ResponseEntity createUser(@RequestBody CreateUserRequestModel requestModel){
  ModelMapper modelMapper = new ModelMapper();
  UserDto userDto = modelMapper.map(requestModel, UserDto.class);
  modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
  UserDto userDtoResponse =usersService.createuser(userDto);
  CreateUserResponseModel responseModel = modelMapper.map(userDtoResponse,CreateUserResponseModel.class);
  return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);
 }

 @GetMapping(value="/{userId}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
 public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

  UserDto userDto = usersService.getUserByUserId(userId);
  UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

  return ResponseEntity.status(HttpStatus.OK).body(returnValue);
 }
}