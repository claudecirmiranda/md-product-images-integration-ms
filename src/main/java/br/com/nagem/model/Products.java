package br.com.nagem.model;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.nagem.dto.Altcor;
import br.com.nagem.dto.Altesp;
import br.com.nagem.dto.Altkit;
import br.com.nagem.dto.Altsev;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@Document("products")
@ToString
public class Products {

	private String action;

	private String branchId;

	@Id
	private String productId;

	private String descriptionForInternet;

	private String brandId;

	private String brandDescription;

	private String groupId;

	private String groupDescription;

	private String businessTextForInternet;

	private String launchStatus;

	private double grossWeight;

	private double volume;

	private double b2cListPrice;

	private double storeListPrice;

	private String promotionalStatus;

	private double promocionalDiscountPercentage;

	private double promotionPriceB2c;

	private String promotionalDateTimeStart;

	private String promotionalDateTimeFinish;

	private Integer promotionalMinimumBuy;

	private Integer promotionalMaximumBuy;

	private Integer maxPaymentTerm;

	private String promotionalStoreStatus;

	private double promotionalStoreDiscount;

	private double promotionalStorePrice;

	private Integer promotionalMinimumStore;

	private Integer promotionalMaximumStore;

	private Integer promotionalMaximumPeriodUse;

	private double productBalance;

	private String exclusiveForB2c;

	private String barCode;

	private Integer internetSalesLimit;

	private String preSalesStatus;

	private String salesDateStart;

	private String salesDateFinish;

	private double ipiAliquot;

	private String mediaType;

	private String mediaUrl;

	private String saleType;

	private String title;

	private String subTitle;

	private String supplierProductId;

	private String productStatusB2c;

	private String productStatusStore;

	private String withdrawalStatusB2c;

	private List<Altesp> specifications;

	private String ALTESP_TAG;

	private double heightPacking;

	private double lenghtPacking;

	private double widthPacking;

	private List<Altkit> componentList;

	private String ALTKIT_TAG; 

	private List<Altcor> fixedRelatedProductId;

	private String ALTCOR_TAG; 

	private List<Altsev> serviceList; 

	private String ALTSEV_TAG; 

	private Integer cpfBuyLimit;

	private Integer cpfDateLimit;

	private String statusToOcc;

	private String ds_statusToOcc;
	
//	ECPRDD03 (Pre venda)
	@JsonProperty(value = "preOrderCommercialSegment")
	private String preOrderCommercialSegment;
	@JsonProperty(value = "preOrderQuantity")
	private Integer preOrderQuantity;
	@JsonProperty(value = "preOrderBilledQuantity")
	private Integer preOrderBilledQuantity;
	@JsonProperty(value = "maximumBilled")
	private Integer maximumBilled;
	@JsonProperty(value = "preOrderActive")
	private String preOrderActive;
	@JsonProperty(value = "preOrderEnvId")
	private String preOrderEnvId;
//	private String COPRDD07 (Promoção)
	@JsonProperty(value = "saleQuantity")
	private Integer saleQuantity;
	@JsonProperty(value = "saleItemBalance")
	private Integer saleItemBalance;
	@JsonProperty(value = "saleCommercialSegment")
	private String saleCommercialSegment;
	@JsonProperty(value = "salePrice")
	private Double salePrice;
	
//	private String COPRDD00
	@JsonProperty(value = "measurementUnit")
	private String measurementUnit;
	@JsonProperty(value = "currentBalance")
	private Integer currentBalance;
	@JsonProperty(value = "b2cBalance")
	private Integer b2cBalance;
	@JsonProperty(value = "depositBalance")
	private Integer depositBalance;
//	private String COPRDD05
	@JsonProperty(value = "b2cBlockedBalance")
	private Integer b2cBlockedBalance;
	@JsonProperty(value = "customerReservedBalance")
	private Integer customerReservedBalance;
	@JsonProperty(value = "b2cReservedBalance")
	private Integer b2cReservedBalance;
	
}
