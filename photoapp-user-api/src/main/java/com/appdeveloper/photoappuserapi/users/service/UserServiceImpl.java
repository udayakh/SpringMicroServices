package com.appdeveloper.photoappuserapi.users.service;

import com.appdeveloper.photoappuserapi.ui.model.AlbumResponseModel;
import com.appdeveloper.photoappuserapi.users.data.AlbumsServiceClient;
import com.appdeveloper.photoappuserapi.users.data.UserEntity;
import com.appdeveloper.photoappuserapi.users.data.UsersRepository;
import com.appdeveloper.photoappuserapi.users.shared.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UsersService{
    UsersRepository usersRepository;
    BCryptPasswordEncoder passwordEncoder;
    //AlbumsServiceClient albumsServiceClient;
    Environment environment;
    AlbumsServiceClient albumsServiceClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder passwordEncoder,
                           Environment environment, AlbumsServiceClient albumsServiceClient) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.environment = environment;
        this.albumsServiceClient = albumsServiceClient;
    }



    @Override
    public UserDto createuser(UserDto userDto) {
        userDto.setUserNumber(UUID.randomUUID().toString());
        userDto.setEcryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        UserEntity userEntity1 = usersRepository.save(userEntity);
        UserDto userDto1= modelMapper.map(userEntity1,UserDto.class);
        return userDto1;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity,UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);
        if(userEntity == null) throw new UsernameNotFoundException(username);
        return new User(userEntity.getEmail(),
                userEntity.getEcryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }

    @Override
    public UserDto getUserByUserId(String userNumber) {

        UserEntity userEntity = usersRepository.findByUserNumber(userNumber);
        if(userEntity == null) throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);


        String albumsUrl = String.format(environment.getProperty("albums.url"), userNumber);
/*
        ResponseEntity<List<AlbumResponseModel>> albumsListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<AlbumResponseModel>>() {
        });
        List<AlbumResponseModel> albumsList = albumsListResponse.getBody();*/

        logger.info("Before calling albums Microservice");
        List<AlbumResponseModel> albumsList = albumsServiceClient.getAlbums(userNumber);
        logger.info("After calling albums Microservice");

        userDto.setAlbums(albumsList);

        return userDto;
    }

}
