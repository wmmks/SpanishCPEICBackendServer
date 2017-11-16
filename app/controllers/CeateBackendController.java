package controllers;

import Search.SearchPostProcessing;
import com.fasterxml.jackson.databind.JsonNode;
import constantField.ConstantField;
import constantField.DatabaseColumnNameVariableTable;
import constantField.XMLArticleConstantTable;
import databaseUtil.DatabaseController;
import json.JSONObject;
import play.libs.Json;
import play.mvc.*;

import sqlCommandLogic.SqlCommandComposer;
import sqlCommandLogic.UserData;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by roye on 2017/4/24.
 */
public class CeateBackendController extends Controller{

    @Inject
    SqlCommandComposer sqlCommandComposer;


    public Result HelloWorld()
    {
        return ok("HelloWorld");
    }

    public Result getUserData() {
        DatabaseController databaseController = new DatabaseController();
        JsonNode request = request().body().asJson();
        int id = Integer.parseInt(request.findPath(ConstantField.userAndArticleID).toString());
        int systemType = Integer.parseInt(request.findPath(ConstantField.userAndArticleSystemType).textValue());
        JsonNode result = Json.newObject();
        ResultSet resultSet = databaseController.execSelect(sqlCommandComposer.getUserDataSqlByIdAndSystemType(id, systemType));
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            JSONObject resultJsonObject = new JSONObject();
            if (resultSet.next())
            {
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++)
                {
                    Object columnValue = resultSet.getObject(i);
                    if (resultSetMetaData.getColumnTypeName(i).equals(ConstantField.databaseStringType))
                    {
                        resultJsonObject.put(resultSetMetaData.getColumnName(i), columnValue.toString());
                    }
                    else if (resultSetMetaData.getColumnTypeName(i).equals(ConstantField.databaseIntType))
                    {
                        resultJsonObject.put(resultSetMetaData.getColumnName(i), Integer.parseInt(columnValue.toString()));
                    }
                }
            }
            result = Json.parse(resultJsonObject.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok(result);
    }

    /**
     * Check Page Update User Data Function.
     */
    public Result updateUserData() {
        DatabaseController databaseController = new DatabaseController();
        JsonNode request = request().body().asJson();
        JsonNode result;
        JSONObject userDataJsonObject = new JSONObject(request.toString());
        UserData userData = sqlCommandComposer.getUserData(userDataJsonObject);
        int id = userDataJsonObject.getInt(DatabaseColumnNameVariableTable.id);
        String updateCondition = "where " + DatabaseColumnNameVariableTable.id + "=" + id;
        databaseController.execUpdate(DatabaseColumnNameVariableTable.usersInformationTableName, userData.getUserInformationSqlObject(), updateCondition);
        databaseController.execUpdate(DatabaseColumnNameVariableTable.articlesInformationTableName, userData.getArticleInformationSqlObject(), updateCondition);
        databaseController.execUpdate(DatabaseColumnNameVariableTable.classInformationTableName, userData.getClassInformationSqlObject(), updateCondition);
        databaseController.execUpdate(DatabaseColumnNameVariableTable.usersSpecialExperienceTableName, userData.getUserSpecialExperienceSqlObject(), updateCondition);
        if (!XMLArticleConstantTable.xmlErrorFlag) {
            databaseController.execUpdate(DatabaseColumnNameVariableTable.articlesContentTableName, userData.getArticleContentSqlObject(), updateCondition);
            result = Json.parse(new JSONObject().put("message", "User id=" + id + " data update finish").toString());
        } else {
            result = Json.parse(new JSONObject().put("error_message", "User id=" + id + " xml format is incorrect, please fix it and re-click update.").toString());
        }
        XMLArticleConstantTable.xmlErrorFlag = false;
        return ok(result);
    }

    /**
     * Search Page Extract Sentence List Function.
     */
    public Result getSearchData() {
        JsonNode result = Json.newObject();
        JsonNode request = request().body().asJson();
        JSONObject userDataJsonObject = new JSONObject(request.toString());
        String wordText = userDataJsonObject.getString(ConstantField.WORD_TEXT);
        String wordPOS = userDataJsonObject.getString(ConstantField.WORD_POS);
        String nextWordPOS = userDataJsonObject.getString(ConstantField.NEXT_WORD_POS);
        SearchPostProcessing searchPostProcessing = new SearchPostProcessing();
        try {
            result = searchPostProcessing.setSearchPostProcessing(wordText, wordPOS, nextWordPOS);
        } catch (SQLException e) {
            e.getErrorCode();
        }
        return ok(result);
    }

    /**
     * Search Page Extract Original Article and Correct Article Function.
     */
    public Result getSearchXMLResult() {
        JsonNode request = request().body().asJson();
        JSONObject userDataJsonObject = new JSONObject(request.toString());
        String articleID = userDataJsonObject.getString(ConstantField.ARTICLE_ID);
        return null;
    }
}
