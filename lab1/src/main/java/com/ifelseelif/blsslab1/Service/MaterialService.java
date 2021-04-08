package com.ifelseelif.blsslab1.Service;

import com.ifelseelif.blsslab1.Database.*;
import com.ifelseelif.blsslab1.Models.DTO.*;
import com.ifelseelif.blsslab1.Models.Domain.*;
import com.ifelseelif.blsslab1.Service.Interface.IMaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MaterialService implements IMaterialService {
    private Logger logger = LoggerFactory.getLogger(MaterialService.class);

    private final MaterialRepository materialRepository;
    private final StoryRepository storyRepository;
    private final BlogRepository blogRepository;
    private final ReviewRepository reviewRepository;
    private final CountryRepository countryRepository;
    private final HotelRepository hotelRepository;

    public MaterialService(MaterialRepository materialRepository, StoryRepository storyRepository,
                           BlogRepository blogRepository, ReviewRepository reviewRepository,
                           CountryRepository countryRepository, HotelRepository hotelRepository) {
        this.materialRepository = materialRepository;
        this.storyRepository = storyRepository;
        this.blogRepository = blogRepository;
        this.reviewRepository = reviewRepository;
        this.countryRepository = countryRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<DbMaterial> getAllMaterials() {
        return (List<DbMaterial>) materialRepository.findAll();
    }

    @Override
    public DbMaterial getMaterial(long id) {
        return materialRepository.getDbMaterialById(id);
    }

    @Override
    public void publishMaterial(int id, TypeMaterial typeMaterial) {
        throw new NotImplementedException();
    }

    @Override
    public void deleteMaterial(long id) {
        throw new NotImplementedException();
    }

    @Override
    public void createMaterial(TypeMaterial typeMaterial) {
        DbMaterial dbMaterial = new DbMaterial();
        dbMaterial.setTypeMaterial(typeMaterial);
        dbMaterial.setStatus(Status.Draft);
        switch (typeMaterial) {
            case Blog:
                dbMaterial.setBlog(new DbBlog(blogRepository.findLastBlog()));
                break;
            case Story:
                dbMaterial.setStory(new DbStory(storyRepository.findLastStory()));
                break;
            case Review:
                dbMaterial.setReview(new DbReview(reviewRepository.findLastReview()));
                break;
        }

        materialRepository.save(dbMaterial);
    }

    @Override
    public void createBlog(Blog blog) {
        DbBlog dbBlog = new DbBlog();
        dbBlog.setHeader(blog.getHeader());
        dbBlog.setBriefInformation(blog.getBriefInformation());
        dbBlog.setMainText(blog.getMainText());
        dbBlog.setCountry(new DbCountry(blog.getCountryId()));
        dbBlog.setChecked(false);

        blogRepository.save(dbBlog);

        this.createMaterial(TypeMaterial.Blog);
    }

    @Override
    public void createReview(Review review) {
        DbReview dbReview = new DbReview();
        dbReview.setScoreOfLocation(review.getScoreOfLocation());
        dbReview.setScoreOfService(review.getScoreOfService());
        dbReview.setScoreOfStuff(review.getScoreOfStuff());
        dbReview.setScoreOfFood(review.getScoreOfFood());
        dbReview.setGoodHotel(review.getIsGoodHotel());
        dbReview.setVisitedDate(review.getVisitedDate());
        dbReview.setAdvantages(review.getAdvantages());
        dbReview.setDisadvantages(review.getDisadvantages());
        dbReview.setMainText(review.getMainText());
        dbReview.setHotel(new DbHotel(review.getHotelId()));

        reviewRepository.save(dbReview);

        this.createMaterial(TypeMaterial.Review);
    }

    @Override
    public void createStory(Story story) {
        DbStory dbStory = new DbStory();
        dbStory.setHeader(story.getHeader());
        dbStory.setTravelDate(story.getTravelDate());
        dbStory.setBriefInformation(story.getBriefInformation());
        dbStory.setMainText(story.getMainText());
        dbStory.setVerified(false);

        Set<DbCountry> countrySet = new HashSet<>();
        for (long countryId : story.getCountries()) {
            countrySet.add(new DbCountry(countryId));
        }

        dbStory.setCountry(countrySet);
        storyRepository.save(dbStory);

        this.createMaterial(TypeMaterial.Story);
    }

    @Override
    public void updateBlog(long id, Blog blog) {
        DbBlog dbBlog = blogRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ("Blog with id=" + id + " not found")
        ));

        DbCountry country = countryRepository.findById(blog.getCountryId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ("Country with id=" + id + " not found")
                ));

        dbBlog.setHeader(blog.getHeader());
        dbBlog.setChecked(blog.isChecked());
        dbBlog.setPublishDate(blog.getPublishDate());
        dbBlog.setMainText(blog.getMainText());
        dbBlog.setCountry(country);

        blogRepository.save(dbBlog);
    }

    @Override
    public void updateReview(long id, Review review) {
        DbReview dbReview = reviewRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ("Review with id=" + id + " not found")
        ));

        DbHotel hotel = hotelRepository.findById(review.getHotelId()).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND, ("Hotel with id=" + id + " not found")
                ));

        dbReview.setAdvantages(review.getAdvantages());
        dbReview.setDisadvantages(review.getDisadvantages());

        dbReview.setVisitedDate(review.getVisitedDate());
        dbReview.setGoodHotel(review.getIsGoodHotel());
        dbReview.setMainText(review.getMainText());
        dbReview.setHotel(hotel);

        dbReview.setScoreOfService(review.getScoreOfService());
        dbReview.setScoreOfFood(review.getScoreOfFood());
        dbReview.setScoreOfStuff(review.getScoreOfStuff());
        dbReview.setScoreOfLocation(review.getScoreOfLocation());

        reviewRepository.save(dbReview);
    }

    @Override
    public void updateStory(long id, Story story) {
        DbStory dbStory = storyRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ("Story with id=" + id + " not found")
        ));

        Iterable<DbCountry> dbCountries = countryRepository.findAllById(story.getCountries());

        HashSet<DbCountry> countries = new HashSet<>();
        dbCountries.iterator().forEachRemaining(countries::add);

        if(countries.size() != story.getCountries().size()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ("Some country not found")
            );
        }

        dbStory.setBriefInformation(story.getBriefInformation());
        dbStory.setMainText(story.getMainText());
        dbStory.setTravelDate(story.getTravelDate());
        dbStory.setHeader(story.getHeader());
        dbStory.setCountry(countries);

        storyRepository.save(dbStory);
    }

    @Override
    public String sendMaterial(long id) {

        Optional<DbMaterial> dbMaterial = materialRepository.findById(id);

        if (dbMaterial.isPresent()) {
            if (!dbMaterial.get().getStatus().equals(Status.Draft)) {
                return "Incorrect status of material";
            }
        } else {
            return "Material not found";
        }

        TypeMaterial typeMaterial = materialRepository.findTypeMaterialById(id);

        if (typeMaterial.equals(TypeMaterial.Story)) {
            materialRepository.changeStatus(id, Status.Published);
        } else {
            materialRepository.changeStatus(id, Status.Approving);
        }

        return "OK";
    }
}
