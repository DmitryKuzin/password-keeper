package com.passwordkeeper.subscription;

import com.passwordkeeper.payment.Payment;
import com.passwordkeeper.payment.PaymentMockRepository;
import com.passwordkeeper.user.UserAccountType;
import com.passwordkeeper.user.UserService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

import static com.passwordkeeper.payment.PaymentStatus.PAYED;
import static com.passwordkeeper.utils.DateUtils.dateToString;
import static com.passwordkeeper.utils.IdGenerator.generateId;
import static org.apache.commons.lang.StringUtils.isEmpty;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private PaymentMockRepository paymentRepository;
    @Autowired
    private SubscriptionCheckService subscriptionCheckService;
    @Autowired
    private UserService userService;

    public SubscriptionDto getSubscriptionByUserId(String userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        if (subscription != null) {
            if (!subscriptionCheckService.isSubscriptionValid(subscription)) {
                subscription.setStatus(SubscriptionStatus.DISABLED.name());
                subscriptionRepository.save(subscription);
                userService.updateUserAccountTypeByUserId(userId, UserAccountType.FREE);
            }
            return SubscriptionDto.builder()
                    .expirationDate(dateToString(subscription.getExpirationDate()))
                    .status(SubscriptionStatus.valueOf(subscription.getStatus()))
                    .subscriptionId(subscription.getSubscriptionId())
                    .paymentId(subscription.getPaymentId())
                    .build();
        }
        return SubscriptionDto.builder().status(SubscriptionStatus.NOT_FOUND).build();
    }

    public SubscriptionDto checkPayment(SubscriptionRequestDto subscriptionRequestDto) {
        if (subscriptionRequestDto == null || isEmpty(subscriptionRequestDto.getSubscriptionId()) ||
                isEmpty(subscriptionRequestDto.getPaymentId())) {
            throw new RuntimeException("Not enough data to perform this operation");
        }
        Optional<Subscription> result = subscriptionRepository.findById(subscriptionRequestDto.getSubscriptionId());
        if (result.isPresent()) {
            Subscription subscription = result.get();
            Payment paymentInfo = paymentRepository.getPaymentInfo(subscription.getPaymentId());
            if (PAYED == paymentInfo.getPaymentStatus()) {
                subscription.setStatus(SubscriptionStatus.ENABLED.name());
                Subscription saved = subscriptionRepository.save(subscription);
                userService.updateUserAccountTypeByUserId(saved.getUserId(), UserAccountType.PREMIUM);
                return SubscriptionDto.builder()
                        .subscriptionId(saved.getSubscriptionId())
                        .paymentId(saved.getPaymentId())
                        .status(SubscriptionStatus.valueOf(saved.getStatus()))
                        .expirationDate(dateToString(saved.getExpirationDate()))
                        .build();
            }
        }
        throw new IllegalStateException("Cannot find subscription");
    }

    public SubscriptionDto requestSubscriptionCreation(SubscriptionRequestDto subscriptionRequestDto) {
        Subscription alreadyExistingSubscription = subscriptionRepository.findByUserId(subscriptionRequestDto.getUserId());
        if (subscriptionCheckService.isSubscriptionValid(alreadyExistingSubscription)) {
            throw new IllegalStateException("User already has subscription");
        }
        String paymentId = paymentRepository.requestPayment();
        Subscription saved = subscriptionRepository.save(Subscription.builder()
                .subscriptionId(generateId())
                .status(SubscriptionStatus.PAYMENT_IN_PROCESS.name())
                .userId(subscriptionRequestDto.getUserId())
                .paymentId(paymentId)
                .expirationDate(getExpirationDateBySubscriptionPeriod(subscriptionRequestDto.getSubscriptionPeriod()))
                .build());

        return SubscriptionDto.builder()
                .status(SubscriptionStatus.PAYMENT_IN_PROCESS)
                .subscriptionId(saved.getSubscriptionId())
                .expirationDate(dateToString(saved.getExpirationDate()))
                .paymentId(paymentId).build();
    }

    private Date getExpirationDateBySubscriptionPeriod(SubscriptionPeriod subscriptionPeriod) {
        switch (subscriptionPeriod) {
            case HOUR:
                return DateUtils.addHours(new Date(), 1);
            case DAY:
                return DateUtils.addDays(new Date(), 1);
            case MONTH:
                return DateUtils.addMonths(new Date(), 1);
            case YEAR:
                return DateUtils.addYears(new Date(), 1);
            default:
                throw new IllegalArgumentException("This case + " + subscriptionPeriod.name() + " not supported");
        }
    }

    public SubscriptionDto requestSubscriptionDeletion(String userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        subscriptionRepository.delete(subscription);
        return SubscriptionDto.builder()
                .subscriptionId(subscription.getSubscriptionId())
                .paymentId(subscription.getPaymentId())
                .status(SubscriptionStatus.DISABLED)
                .build();
    }

}
