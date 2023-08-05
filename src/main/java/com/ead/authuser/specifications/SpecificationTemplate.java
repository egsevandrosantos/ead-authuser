package com.ead.authuser.specifications;

import com.ead.authuser.models.User;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {
    @And({
        @Spec(path = "type", spec = EqualIgnoreCase.class),
        @Spec(path = "status", spec = EqualIgnoreCase.class),
        @Spec(path = "email", spec = EqualIgnoreCase.class),
        @Spec(path = "name", spec = LikeIgnoreCase.class)
    })
    public interface UserSpec extends Specification<User> {}
}
