package com.ctriposs.soa.module;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.leansoft.mxjc.builder.ClassModelBuilder;
import com.leansoft.mxjc.model.CGConfig;
import com.leansoft.mxjc.model.CGModel;
import com.leansoft.mxjc.model.ClassInfo;
import com.leansoft.mxjc.model.EnumInfo;
import com.leansoft.mxjc.model.FieldInfo;
import com.leansoft.mxjc.model.FileInfo;
import com.leansoft.mxjc.model.TypeInfo;
import com.leansoft.mxjc.module.AbstractClientModule;
import com.leansoft.mxjc.module.ModuleName;
import com.leansoft.mxjc.module.XjcModuleException;
import com.leansoft.mxjc.module.pico.PicoType;
import com.leansoft.mxjc.util.ClassNameUtil;

import freemarker.template.SimpleHash;

public class SoaClientModule extends AbstractClientModule {
	private URL clzIntfTemplate;
	private URL clzImplTempalte;
	private URL enumDeclarationTemplate;
	private URL enumDefinitionTemplate;
	private URL commonHeaderTemplate;

	@Override
	public ModuleName getName() {
		return ModuleName.SOA;
	}

	@Override
	public void init() throws XjcModuleException {
		info("SoaClientModule loading templates");
		loadTemplates();

	}

	private void loadTemplates() throws XjcModuleException {
		clzIntfTemplate = this.getTemplateURL("client-class-interface.fmt");
		clzImplTempalte = this.getTemplateURL("client-class-implementation.fmt");
		enumDeclarationTemplate = this.getTemplateURL("client-enum-declaration.fmt");
		enumDefinitionTemplate = this.getTemplateURL("client-enum-definition.fmt");
		commonHeaderTemplate = this.getTemplateURL("client-common-header.fmt");
	}

	@Override
	public Set<FileInfo> generate(CGModel cgModel, CGConfig config) throws XjcModuleException {
		SimpleHash fmModel = this.getFreemarkerModel();
		Set<FileInfo> targetFileSet = new HashSet<FileInfo>();
		info("Generating the SOA client classes...");
		if (config.soaPrefix == null) {
			warn("No prefix is provided, it's recommended to add prefix for Pico binding to avoid possible conflict");
		}

		String prefix = config.soaPrefix == null ? "" : config.soaPrefix;
		prefixType(cgModel, prefix);
		fmModel.put("group", config.soaServiceGroup);

		String relativePath = null;
		info("Generating classes...");
		for (ClassInfo classInfo : cgModel.getClasses()) {
			convertFieldsType(classInfo);
			fmModel.put("superClassImports", getSuperClassImports(classInfo));
			fmModel.put("fieldClassImports", getFieldImports(classInfo));
			fmModel.put("clazz", classInfo);

			relativePath = ClassNameUtil.packageNameToPath(classInfo.getPackageName());
			FileInfo classIntf = generateFile(clzIntfTemplate, fmModel, classInfo.getName(), "h", relativePath);
			FileInfo classImpl = generateFile(clzImplTempalte, fmModel, classInfo.getName(), "m", relativePath);
			targetFileSet.add(classIntf);
			targetFileSet.add(classImpl);
		}

		info("Generating enums...");
		for (EnumInfo enumInfo : cgModel.getEnums()) {
			fmModel.put("enum", enumInfo);

			relativePath = ClassNameUtil.packageNameToPath(enumInfo.getPackageName());
			FileInfo enumDef = generateFile(enumDefinitionTemplate, fmModel, enumInfo.getName(), "m", relativePath);
			FileInfo enumDecl = generateFile(enumDeclarationTemplate, fmModel, enumInfo.getName(), "h", relativePath);
			targetFileSet.add(enumDef);
			targetFileSet.add(enumDecl);
		}

		info("Generatig common header...");
		fmModel.put("classes", cgModel.getClasses());
		fmModel.put("enums", cgModel.getEnums());

		if (relativePath == null) {
			relativePath = "";
		}
		relativePath += File.pathSeparator + "common";
		String commonTypeFileName = prefix + "CommonTypes";
		FileInfo commonHeader = this.generateFile(commonHeaderTemplate, fmModel, commonTypeFileName, "h", relativePath);
		targetFileSet.add(commonHeader);

		return targetFileSet;
	}

	private Object getFieldImports(ClassInfo classInfo) {
		Set<String> imports = new HashSet<String>();
		for (FieldInfo fieldInfo : classInfo.getFields()) {
			TypeInfo fieldClassType = fieldInfo.getType();
			imports.add(fieldClassType.getFullName());
		}
		return imports;
	}

	private Object getSuperClassImports(ClassInfo classInfo) {
		Set<String> imports = new HashSet<String>();
		//TODO extends super class?
		if (classInfo.getSuperClass() != null) {
			TypeInfo superClassType = classInfo.getSuperClass();
			imports.add(superClassType.getFullName());
		}
		return imports;
	}

	private void convertFieldsType(ClassInfo classInfo) {
		for (FieldInfo field : classInfo.getFields()) {
			TypeInfo fieldType = field.getType();
			convertType(fieldType);
			for (TypeInfo paramType : fieldType.getTypeParameters()) {
				convertType(paramType);
			}
		}
	}

	private void convertType(TypeInfo fieldType) {
		if (fieldType == null)
			return;
		String primitiveType = OCTypeMapper.lookupWrapper(fieldType.getFullName());
		if (primitiveType != null) {
			fieldType.setFullName(OCTypeMapper.lookupWrapper(primitiveType));
			fieldType.setName(primitiveType);
			fieldType.setPrimitive(true);
			return;
		} else if (fieldType.isEnum()) {
			fieldType.setName(PicoType.ENUM);
			fieldType.setPrimitive(true);
			return;
		}
		//TODO add fieldtype name
		fieldType.setPrimitive(false);
	}

	private void prefixType(CGModel cgModel, String prefix) {
		//TODO use interface
		for (ClassInfo classInfo : cgModel.getClasses()) {
			classInfo.setName(prefix + classInfo.getName());
			String fullName = classInfo.getPackageName() + "." + classInfo.getName();
			classInfo.setFullName(fullName);
		}

		for (EnumInfo enumInfo : cgModel.getEnums()) {
			enumInfo.setDocComment(prefix + enumInfo.getName());
			String fullName = enumInfo.getPackageName() + "." + enumInfo.getName();
			enumInfo.setFullName(fullName);
		}

		for (ClassInfo classInfo : cgModel.getClasses()) {
			if (classInfo.getSuperClass() != null) {
				TypeInfo superType = classInfo.getSuperClass();
				prefixType(superType, prefix);
			}

			for (FieldInfo field : classInfo.getFields()) {
				TypeInfo fieldType = field.getType();
				prefixType(fieldType, prefix);
				if (fieldType.isArray()) {
					prefixType(fieldType.getElementType(), prefix);
				}

				for (TypeInfo paramType : fieldType.getTypeParameters()) {
					prefixType(paramType, prefix);
				}
			}
		}
	}

	private void prefixType(TypeInfo type, String prefix) {
		if (type == null)
			return;
		if (OCTypeMapper.lookupWrapper(type.getFullName()) != null) {
			return;
		}
		String name = type.getName();
		type.setName(prefix + name);
		type.setFullName(prefix + name);
	}

	@Override
	protected URL getTemplateURL(String template) throws XjcModuleException {
		URL url = SoaClientModule.class.getResource("template/" + template);
		if (url == null) {
			throw new XjcModuleException("Fail to load required template file : " + template);
		}
		debug("SoaClientModule get template : " + url.toString());
		return url;
	}

}
