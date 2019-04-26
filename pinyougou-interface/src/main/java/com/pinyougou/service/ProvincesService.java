package com.pinyougou.service;

import com.pinyougou.pojo.Provinces;
import java.util.List;
import java.io.Serializable;
/**
 * ProvincesService 服务接口
 * @date 2019-03-28 09:58:00
 * @version 1.0
 */
public interface ProvincesService {

	/** 添加方法 */
	void save(Provinces provinces);

	/** 修改方法 */
	void update(Provinces provinces);

	/** 根据主键id删除 */
	void delete(Serializable id);

	/** 批量删除 */
	void deleteAll(Serializable[] ids);

	/** 根据主键id查询 */
	Provinces findOne(Serializable id);

	/** 查询全部 */
	List<Provinces> findAll();

	/** 多条件分页查询 */
	List<Provinces> findByPage(Provinces provinces, int page, int rows);

	/** 根据父级id查询省份 */
    List<Provinces> findProvinceByParentId(String provinceId);
}