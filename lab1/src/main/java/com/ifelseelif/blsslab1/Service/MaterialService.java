package com.ifelseelif.blsslab1.Service;

import com.ifelseelif.blsslab1.Database.*;
import com.ifelseelif.blsslab1.Models.DTO.*;
import com.ifelseelif.blsslab1.Models.Domain.*;
import com.ifelseelif.blsslab1.Service.Interface.IMaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MaterialService implements IMaterialService {
    private Logger logger = LoggerFactory.getLogger(MaterialService.class);

    private final MaterialRepository materialRepository;

    private final StoryRepository storyRepository;
    private final BlogRepository blogRepository;
    private final ReviewRepository reviewRepository;

    private final CountryRepository countryRepository;

    public MaterialService(MaterialRepository materialRepository, StoryRepository storyRepository,
                           BlogRepository blogRepository, ReviewRepository reviewRepository,
                           CountryRepository countryRepository) {
        this.materialRepository = materialRepository;
        this.storyRepository = storyRepository;
        this.blogRepository = blogRepository;
        this.reviewRepository = reviewRepository;
        this.countryRepository = countryRepository;
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
    public void deleteMaterial(int id) {
        throw new NotImplementedException();
    }

//    @Override
//    public void updateMaterial(int id, Material material) {
//        throw new NotImplementedException();
//    }

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
        dbBlog.setPublishDate(blog.getPublishDate());
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

        Set<DbCountry> countrySet = new HashSet<>();
        for (long countryId: story.getCountries()) {
            countrySet.add(new DbCountry(countryId));
        }

        dbStory.setCountry(countrySet);
        storyRepository.save(dbStory);

        this.createMaterial(TypeMaterial.Story);
    }

    @Override
    public void updateBlog(int id, Blog blog) {

    }

    @Override
    public void updateReview(int id, Review review) {

    }

    @Override
    public void updateStory(int id, Story story) {

    }

    @Override
    public void sendMaterial(int id) {

    }
}
