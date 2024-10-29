package vnpt.project.Caller_management.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "cdr",indexes = {
    @Index(name="index_dialNumber", columnList = "dial_number"),
        @Index(name = "index_msisdn", columnList = "msisdn")
})

public class Cdr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "site_id")
    private int siteId;

    @Column(name = "dial_number")
    private String dialNumber;

    @Column(name = "extension")
    private String extension;

    @Column(name = "msisdn")
    private String msisdn;

    @Column(name = "answer_state")
    private String answerState;

    @Column(name = "customer_state")
    private String customerState;

    @Column(name = "call_direction")
    private String callDirection;

    @Column(name = "uuid_map")
    private String uuidMap;

    @Column(name = "transferred")
    private int transferred;

    @Column(name = "msisdn_digits")
    private String msisdnDigits;

    @Column(name = "agent_digits")
    private String agentDigits;

    @Column(name = "xml_path")
    private String xmlPath;

    @Column(name = "recording_path")
    private String recordingPath;

    @Column(name = "client_id")
    private int clientId;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @Column(name = "location")
    private String location;

    @Column(name = "location_tolerance")
    private String locationTolerance;

    @Column(name = "autocall_campaign_id")
    private int autocallCampaignId;

    @Column(name = "address")
    private String address;

    @Column(name = "tapping_id")
    private Integer  tappingId;

    @Column(name = "status")
    private int status;

    @Column(name = "call_length")
    private Long callLength;

    @Column(name = "start_timestamp")
    private String startTimestamp;

    @Column(name = "ring_timestamp")
    private String ringTimestamp;

    @Column(name = "answer_timestamp")
    private String answerTimestamp;

    @Column(name = "end_timestamp")
    private String endTimestamp;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "created_by")
    private Integer createdBy;


//    @Column(name = "updated_at")
//    private Integer   updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_spam")
    private int isSpam;

    @Column(name = "disposition")
    private String disposition;

    @Column(name = "early_to_time")
    private int earlyToTime;

    @Column(name = "talk_time")
    private Integer  talkTime;

    @Column(name = "call_wait")
    private int callWait;

    @Column(name = "source")
    private int source;

//    @Column(name = "source_app", columnDefinition = "TINYINT")
//    private Byte sourceApp;
}