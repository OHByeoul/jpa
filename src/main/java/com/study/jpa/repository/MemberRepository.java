package com.study.jpa.repository;

import com.study.jpa.dto.MemberDto;
import com.study.jpa.entity.Member;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom {
    List<Member> findTop5By();

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select new com.study.jpa.dto.MemberDto(m.id,m.username,t.name) from Member m join m.team t")
    List<MemberDto> findUserByMemberDto();

    @Query("select m from Member m where username in :names")
    List<Member> findUserByName(@Param("names") List<String> names);

    @Query("select m from Member m where id = :id")
    Member findUserById(@Param("id") Long id);

    @Query("select m from Member m where username = :username")
    Member findMemberByName(@Param("username") String username);

    @Query("select m from Member m where age = :age")
    List<Member> findMemberListByAge(@Param("age") int age);

    @Query("select m from Member m where username = :username")
    Optional<Member> findOptionalMemberByName(@Param("username") String username);

    Slice<Member> findByAge(int age, Pageable pageable);

    Page<Member> findUserForPageByUsername(String username, Pageable pageable);

    @Modifying(clearAutomatically = true) // 변경할때는 무조건 써줘야함 안써주면 executeUpdate를 하는 것이 아닌 getSingleResult나 getResultList가 호출된다.
                                        //clearAutomatically를 써주면 em.clear()가 호출되면서 1차 캐시를 비운다.
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkUpdate(@Param("age") int age);

    @EntityGraph(attributePaths = "team")
    @Override
    List<Member> findAll();

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true")) // setxx를 통해 값을 변경하더라도 디비에 업데이트 쿼리가 날라가지 않음
    List<Member> findMemberByUsername(@Param("username") String name);

   @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findMemberLockByUsername(@Param("username") String name);

   List<UsernameOnly> findProjectionsByUsername(@Param("username") String name);

    List<UsernameOnlyDto> findProjections2ByUsername(@Param("username") String name);
}
