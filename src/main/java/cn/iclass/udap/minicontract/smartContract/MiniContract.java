package cn.iclass.udap.minicontract.smartContract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class MiniContract extends Contract {
    private static final String BINARY = "6060604052341561000f57600080fd5b60008054600160a060020a033316600160a060020a03199091161790556112718061003b6000396000f30060606040526004361061006c5763ffffffff7c01000000000000000000000000000000000000000000000000000000006000350416631f4a37a58114610071578063404139251461023a578063971d852f1461045b578063b16daca114610471578063d10173dd14610515575b600080fd5b341561007c57600080fd5b6100876004356106e9565b6040518080602001806020018060200180602001858103855289818151815260200191508051906020019080838360005b838110156100d05780820151838201526020016100b8565b50505050905090810190601f1680156100fd5780820380516001836020036101000a031916815260200191505b50858103845288818151815260200191508051906020019080838360005b8381101561013357808201518382015260200161011b565b50505050905090810190601f1680156101605780820380516001836020036101000a031916815260200191505b50858103835287818151815260200191508051906020019080838360005b8381101561019657808201518382015260200161017e565b50505050905090810190601f1680156101c35780820380516001836020036101000a031916815260200191505b50858103825286818151815260200191508051906020019080838360005b838110156101f95780820151838201526020016101e1565b50505050905090810190601f1680156102265780820380516001836020036101000a031916815260200191505b509850505050505050505060405180910390f35b341561024557600080fd5b61045960046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f0160208091040260200160405190810160405281815292919060208401838380828437509496506109d495505050505050565b005b341561046657600080fd5b610459600435610ca8565b341561047c57600080fd5b6104c260046024813581810190830135806020601f82018190048102016040519081016040528181529291906020840183838082843750949650610d2695505050505050565b60405160208082528190810183818151815260200191508051906020019060200280838360005b838110156105015780820151838201526020016104e9565b505050509050019250505060405180910390f35b341561052057600080fd5b61052b600435610de9565b60405180806020018060200180602001806020018615151515815260200185810385528a818151815260200191508051906020019080838360005b8381101561057e578082015183820152602001610566565b50505050905090810190601f1680156105ab5780820380516001836020036101000a031916815260200191505b50858103845289818151815260200191508051906020019080838360005b838110156105e15780820151838201526020016105c9565b50505050905090810190601f16801561060e5780820380516001836020036101000a031916815260200191505b50858103835288818151815260200191508051906020019080838360005b8381101561064457808201518382015260200161062c565b50505050905090810190601f1680156106715780820380516001836020036101000a031916815260200191505b50858103825287818151815260200191508051906020019080838360005b838110156106a757808201518382015260200161068f565b50505050905090810190601f1680156106d45780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390f35b6106f16110f3565b6106f96110f3565b6107016110f3565b6107096110f3565b600160008681526020019081526020016000206005018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107b35780601f10610788576101008083540402835291602001916107b3565b820191906000526020600020905b81548152906001019060200180831161079657829003601f168201915b50505050509350600160008681526020019081526020016000206006018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156108645780601f1061083957610100808354040283529160200191610864565b820191906000526020600020905b81548152906001019060200180831161084757829003601f168201915b50505050509250600160008681526020019081526020016000206007018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109155780601f106108ea57610100808354040283529160200191610915565b820191906000526020600020905b8154815290600101906020018083116108f857829003601f168201915b50505050509150600160008681526020019081526020016000206008018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156109c65780601f1061099b576101008083540402835291602001916109c6565b820191906000526020600020905b8154815290600101906020018083116109a957829003601f168201915b505050505090509193509193565b6109dc611105565b6000543373ffffffffffffffffffffffffffffffffffffffff908116911614610a0457600080fd5b610120604051908101604052808a815260200189815260200188815260200187815260200160001515815260200186815260200185815260200184815260200183815250905080600160006002548152602001908152602001600020600082015181908051610a77929160200190611181565b50602082015181600101908051610a92929160200190611181565b50604082015181600201908051610aad929160200190611181565b50606082015181600301908051610ac8929160200190611181565b50608082015160048201805460ff191691151591909117905560a082015181600501908051610afb929160200190611181565b5060c082015181600601908051610b16929160200190611181565b5060e082015181600701908051610b31929160200190611181565b5061010082015181600801908051610b4d929160200190611181565b509050506003896040518082805190602001908083835b60208310610b835780518252601f199092019160209182019101610b64565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051908190039020805460018101610bc783826111ff565b50600091825260209091206002549101556003886040518082805190602001908083835b60208310610c0a5780518252601f199092019160209182019101610beb565b6001836020036101000a038019825116818451168082178552505050505050905001915050908152602001604051908190039020805460018101610c4e83826111ff565b506000918252602090912060025491018190557fd3b769a2937619e52217a71926fa1771bc08121dfc62429ef7c72dca27e84e239060405190815260200160405180910390a1505060028054600101905550505050505050565b6000543373ffffffffffffffffffffffffffffffffffffffff908116911614610cd057600080fd5b600081815260016020819052604091829020600401805460ff191690911790557fd0e55388c46e2da0ce466e229fea0cdd881aaaab1c1af0411dad1071221437fe9082905190815260200160405180910390a150565b610d2e6110f3565b6003826040518082805190602001908083835b60208310610d605780518252601f199092019160209182019101610d41565b6001836020036101000a0380198251168184511680821785525050505050509050019150509081526020016040518091039020805480602002602001604051908101604052809291908181526020018280548015610ddd57602002820191906000526020600020905b815481526020019060010190808311610dc9575b50505050509050919050565b610df16110f3565b610df96110f3565b610e016110f3565b610e096110f3565b6000600160008781526020019081526020016000206000018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610eb55780601f10610e8a57610100808354040283529160200191610eb5565b820191906000526020600020905b815481529060010190602001808311610e9857829003601f168201915b50505050509450600160008781526020019081526020016000206001018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610f665780601f10610f3b57610100808354040283529160200191610f66565b820191906000526020600020905b815481529060010190602001808311610f4957829003601f168201915b50505050509350600160008781526020019081526020016000206002018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110175780601f10610fec57610100808354040283529160200191611017565b820191906000526020600020905b815481529060010190602001808311610ffa57829003601f168201915b50505050509250600160008781526020019081526020016000206003018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156110c85780601f1061109d576101008083540402835291602001916110c8565b820191906000526020600020905b8154815290600101906020018083116110ab57829003601f168201915b505050600098895250506001602052604090962060040154949693959294929360ff90931692915050565b60206040519081016040526000815290565b6101206040519081016040528061111a6110f3565b81526020016111276110f3565b81526020016111346110f3565b81526020016111416110f3565b8152600060208201526040016111556110f3565b81526020016111626110f3565b815260200161116f6110f3565b815260200161117c6110f3565b905290565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106111c257805160ff19168380011785556111ef565b828001600101855582156111ef579182015b828111156111ef5782518255916020019190600101906111d4565b506111fb929150611228565b5090565b81548183558181151161122357600083815260209020611223918101908301611228565b505050565b61124291905b808211156111fb576000815560010161122e565b905600a165627a7a723058208d13200791cb2070db204bedb440396f70365c58e0861cbae71c4baf4ed3ee5e0029";

    protected MiniContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected MiniContract(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<EMintEventResponse> getEMintEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("eMint", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<EMintEventResponse> responses = new ArrayList<EMintEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EMintEventResponse typedResponse = new EMintEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.dbindex = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<EMintEventResponse> eMintEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("eMint", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, EMintEventResponse>() {
            @Override
            public EMintEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                EMintEventResponse typedResponse = new EMintEventResponse();
                typedResponse.log = log;
                typedResponse.dbindex = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ECompleteEventResponse> getECompleteEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("eComplete", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ECompleteEventResponse> responses = new ArrayList<ECompleteEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ECompleteEventResponse typedResponse = new ECompleteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.dbindex = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ECompleteEventResponse> eCompleteEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("eComplete", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ECompleteEventResponse>() {
            @Override
            public ECompleteEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ECompleteEventResponse typedResponse = new ECompleteEventResponse();
                typedResponse.log = log;
                typedResponse.dbindex = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<Tuple4<byte[], byte[], byte[], byte[]>> detailContent(BigInteger _dbindex) {
        final Function function = new Function("detailContent", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_dbindex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}));
        return new RemoteCall<Tuple4<byte[], byte[], byte[], byte[]>>(
                new Callable<Tuple4<byte[], byte[], byte[], byte[]>>() {
                    @Override
                    public Tuple4<byte[], byte[], byte[], byte[]> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<byte[], byte[], byte[], byte[]>(
                                (byte[]) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue());
                    }
                });
    }

    public RemoteCall<TransactionReceipt> mint(byte[] _A, byte[] _B, byte[] _Aphone, byte[] _Bphone, byte[] _title, byte[] _content, byte[] _sound, byte[] _picture) {
        final Function function = new Function(
                "mint", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(_A), 
                new org.web3j.abi.datatypes.DynamicBytes(_B), 
                new org.web3j.abi.datatypes.DynamicBytes(_Aphone), 
                new org.web3j.abi.datatypes.DynamicBytes(_Bphone), 
                new org.web3j.abi.datatypes.DynamicBytes(_title), 
                new org.web3j.abi.datatypes.DynamicBytes(_content), 
                new org.web3j.abi.datatypes.DynamicBytes(_sound), 
                new org.web3j.abi.datatypes.DynamicBytes(_picture)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> complete(BigInteger _dbindex) {
        final Function function = new Function(
                "complete", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_dbindex)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<List> list(byte[] _owner) {
        final Function function = new Function("list", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.DynamicBytes(_owner)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteCall<Tuple5<byte[], byte[], byte[], byte[], Boolean>> detailMeta(BigInteger _dbindex) {
        final Function function = new Function("detailMeta", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_dbindex)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<DynamicBytes>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple5<byte[], byte[], byte[], byte[], Boolean>>(
                new Callable<Tuple5<byte[], byte[], byte[], byte[], Boolean>>() {
                    @Override
                    public Tuple5<byte[], byte[], byte[], byte[], Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<byte[], byte[], byte[], byte[], Boolean>(
                                (byte[]) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (byte[]) results.get(3).getValue(), 
                                (Boolean) results.get(4).getValue());
                    }
                });
    }

    public static RemoteCall<MiniContract> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MiniContract.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<MiniContract> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(MiniContract.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static MiniContract load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new MiniContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static MiniContract load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new MiniContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class EMintEventResponse {
        public Log log;

        public BigInteger dbindex;
    }

    public static class ECompleteEventResponse {
        public Log log;

        public BigInteger dbindex;
    }
}
